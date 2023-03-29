package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage {

    private static int id = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            id++;
            film.setId(id);
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        return films.get(id);
    }
}
