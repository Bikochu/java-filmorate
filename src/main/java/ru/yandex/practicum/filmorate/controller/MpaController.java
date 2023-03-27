package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.MpaStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaStorage mpaStorage;

    public MpaController(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaStorage.getMpaList();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") int id) {
        if (mpaStorage.findMpaById(id) != null) {
            return mpaStorage.findMpaById(id);
        } else {
            throw new MpaNotFoundException("Возрастной рейтинг не найден.");
        }
    }
}
