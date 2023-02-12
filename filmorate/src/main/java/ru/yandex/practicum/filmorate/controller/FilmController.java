package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@Slf4j
@RequestMapping("/films")
@ResponseBody
public class FilmController {

    private static int id = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ArrayList<Film> getFilms() {
        log.info("Колличество фильмов в коллекции: " + films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Строка с именем пустая.");
            throw new ValidationException("Имя фильма не должно быть пустым.");
        }
        if (film.getDescription().length() >= 200) {
            log.error("Длинна строки превышает 200 символов.");
            throw new ValidationException("Максимальная длинна описания 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.error("Дата введена не верно.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.error("Введено отрицательное значение.");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
        if (!films.containsKey(film.getId())) {
            id++;
            film.setId(id);
            log.info("Добавили фильму ID: {} .",id);
        }
        films.put(film.getId(),film);
        log.info("Добавили фильм {} в коллекцию под номером {}.",film.getName(),film.getId());

        return film;
    }

    @PutMapping()
    public Film updateFilm (@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            if (film.getName() == null || film.getName().isBlank()) {
                log.error("Строка с именем пустая.");
                throw new ValidationException("Имя фильма не должно быть пустым.");
            }
            if (film.getDescription().length() >= 200) {
                log.error("Длинна строки превышает 200 символов.");
                throw new ValidationException("Максимальная длинна описания 200 символов.");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
                log.error("Дата введена не верно.");
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
            }
            if (film.getDuration() < 0) {
                log.error("Введено отрицательное значение.");
                throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
            }
            films.put(film.getId(), film);
            log.info("Обновили фильм {} в коллекции.",film.getName());
        } else {
            log.error("Номер фильма в коллекции не существует.");
            throw new ValidationException("Такого фильма не существует.");
        }
        return film;
    }

    public void setId(int id) {
        FilmController.id = id;
    }
}
