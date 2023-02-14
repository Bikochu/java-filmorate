package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/users")
@ResponseBody
public class UserController {
    private static int id = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Колличество пользователей: "+users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            id++;
            user.setId(id);
            log.info("Присваиваем номер {} пользователю.", id);
        }
        users.put(user.getId(), user);
        log.info("Добавляем пользоватля {} в коллекцию под номером {}.", user.getLogin(), user.getId());
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("Номер пользователя в коллекции не существует.");
            throw new ValidationException("Такого пользователя не существует.");
        }
        users.put(user.getId(), user);
        log.info("Обновляем пользоватля {} под номером {} в коллекции.", user.getLogin(), user.getId());
        return user;
    }
}
