package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") int id) {
        return filmService.findFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(
            @RequestParam(defaultValue = "10", required = false) int count
    ) {
        return filmService.getPopular(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addFilmLike(
            @PathVariable("id") int id,
            @PathVariable("userId") int userId
    ) {
        filmService.addFilmLike(id, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeFilmLike(
            @PathVariable("filmId") Integer filmId,
            @PathVariable("userId") Integer userId
    ) throws FilmNotFoundException, UserNotFoundException {
        filmService.removeFilmLike(filmId, userId);
    }
}
