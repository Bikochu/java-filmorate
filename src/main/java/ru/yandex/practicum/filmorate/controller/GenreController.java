package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeption.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.GenreStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreStorage genreStorage;

    public GenreController(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreStorage.getGenreList();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") int id) {
        if (genreStorage.findGenreById(id) != null) {
            return genreStorage.findGenreById(id);
        } else {
            throw new GenreNotFoundException("Жанр не найден.");
        }
    }
}
