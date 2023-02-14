package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public Film addFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            id++;
            film.setId(id);
            log.info("Добавили фильму ID: {} .", id);
        }
        films.put(film.getId(), film);
        log.info("Добавили фильм {} в коллекцию под номером {}.", film.getName(), film.getId());
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("Номер фильма в коллекции не существует.");
            throw new ValidationException("Такого фильма не существует.");
        }
        films.put(film.getId(), film);
        log.info("Обновили фильм {} в коллекции.", film.getName());
        return film;
    }
}
