package ru.yandex.practicum.filmorate.storage.inteface;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User findUserById(int id);
}
