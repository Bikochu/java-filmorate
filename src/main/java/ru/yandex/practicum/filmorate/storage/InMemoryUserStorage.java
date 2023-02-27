package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inteface.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static int id = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            id++;
            user.setId(id);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Такого пользователя не существует.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден.");
        } else {
            return users.get(id);
        }
    }
}
