package ru.yandex.practicum.filmorate.storage.inteface;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film findFilmById(int id);
}
