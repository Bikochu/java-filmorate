package ru.yandex.practicum.filmorate.storage.inteface;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User findUserById(int id);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    void addFilmsLike(int filmId, int userId);

    void removeFilmLike(int filmId, int userId);
}
