package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<Film> getPopular(int count) {
        return inMemoryFilmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addFilmLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        film.setRate(film.getRate()+1);
        inMemoryUserStorage.findUserById(userId).getFilmsLike().add(filmId);
    }

    public void removeFilmLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        film.setRate(film.getRate()-1);
        inMemoryUserStorage.findUserById(userId).getFilmsLike().remove(filmId);
    }

}
