package ru.yandex.practicum.filmorate.storage.inteface;

import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {

    ArrayList<Film> getFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film) throws ValidationException;
    Film findFilmById(int id) throws FilmNotFoundException;
}
