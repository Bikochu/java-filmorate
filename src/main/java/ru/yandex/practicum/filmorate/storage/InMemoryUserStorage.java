package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage {

    private static int id = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            id++;
            user.setId(id);
        }
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        users.put(user.getId(), user);
        return user;
    }

    public User findUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        return users.get(id);
    }
}
