package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.inteface.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int id = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            id++;
            film.setId(id);
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма не существует.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм не найден.");
        } else {
            return films.get(id);
        }
    }
}
