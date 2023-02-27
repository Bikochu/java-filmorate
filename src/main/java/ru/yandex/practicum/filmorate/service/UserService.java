package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getFriends(int id) {
        Set<Integer> usersFriends = inMemoryUserStorage.findUserById(id).getFriends();
        return usersFriends.stream()
                .map(inMemoryUserStorage::findUserById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public List<User> getCommonFriends(int id, int otherId) {
        User user = inMemoryUserStorage.findUserById(id);
        User friend = inMemoryUserStorage.findUserById(otherId);
        List<User> commonFriends = new ArrayList<>();
        if (user.getFriends() != null) {
            Set<Integer> userFriends = inMemoryUserStorage.findUserById(id).getFriends();
            Set<Integer> friendFriends = inMemoryUserStorage.findUserById(otherId).getFriends();
            commonFriends.addAll(userFriends.stream().map(inMemoryUserStorage::findUserById).collect(Collectors.toList()));
            commonFriends.retainAll(friendFriends.stream().map(inMemoryUserStorage::findUserById).collect(Collectors.toList()));
        }
        return commonFriends;
    }

    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0) {
            throw new UserNotFoundException("Номер пользователя не может быть отрицательным.");
        } else {
            inMemoryUserStorage.findUserById(id).getFriends().add(friendId);
            inMemoryUserStorage.findUserById(friendId).getFriends().add(id);
        }
    }

    public void removeFriend(int id, int friendId) {
        inMemoryUserStorage.findUserById(id).getFriends().remove(friendId);
        inMemoryUserStorage.findUserById(friendId).getFriends().remove(id);
    }
}
