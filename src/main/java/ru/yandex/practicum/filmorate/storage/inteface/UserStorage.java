package ru.yandex.practicum.filmorate.storage.inteface;

import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();
    User addUser(User user);
    User updateUser(User user) throws ValidationException;
    User findUserById(int id) throws UserNotFoundException;
}
