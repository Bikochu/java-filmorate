package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public Film findFilmById(int id) {
        return filmDbStorage.findFilmById(id);
    }

    public List<Film> getPopular(int count) {
        return filmDbStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addFilmLike(int filmId, int userId) {
        userDbStorage.addFilmsLike(filmId,userId);
        Film film = filmDbStorage.findFilmById(filmId);
        film.setRate(film.getRate()+1);
        userDbStorage.findUserById(userId).getFilmsLike().add(filmId);
    }

    public void removeFilmLike(int filmId, int userId) {
        userDbStorage.removeFilmLike(filmId,userId);
        Film film = filmDbStorage.findFilmById(filmId);
        film.setRate(film.getRate() - 1);
        userDbStorage.findUserById(userId).getFilmsLike().remove(filmId);
    }

}
