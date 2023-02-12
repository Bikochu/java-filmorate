package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Поле Е-маил пустое или не содержит @");
            throw new ValidationException("Е-маил не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Поле Логин пустое или содержит пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Поле Имя пустое, присваиваем полю имя логина.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (!users.containsKey(user.getId())) {
            id++;
            user.setId(id);
            log.info("Присваиваем номер {} пользователю.",id);
        }
        log.info("Добавляем пользоватля {} в коллекцию под номером {}.",user.getLogin(),user.getId());
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.error("Поле Е-маил пустое или не содержит @");
                throw new ValidationException("Е-маил не может быть пустой и должна содержать символ @");
            }
            if (user.getLogin() == null || user.getLogin().contains(" ")) {
                log.error("Поле Логин пустое или содержит пробелы.");
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                log.info("Поле Имя пустое, присваиваем полю имя логина.");
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть в будущем.");
                throw new ValidationException("Дата рождения не может быть в будущем.");
            }
            log.info("Обновляем пользоватля {} под номером {} в коллекции.",user.getLogin(),user.getId());
            users.put(user.getId(), user);
        } else {
            log.error("Номер пользователя в коллекции не существует.");
            throw new ValidationException("Такого пользователя не существует.");
        }
        return user;
    }

    public void setId(int id) {
        UserController.id = id;
    }
}
