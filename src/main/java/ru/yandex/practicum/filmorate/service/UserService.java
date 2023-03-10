package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inteface.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }

    public List<User> getFriends(int id) {
        Set<Integer> usersFriends = userStorage.findUserById(id).getFriends();
        return usersFriends.stream()
                .map(userStorage::findUserById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User user = userStorage.findUserById(id);
        List<User> commonFriends = new ArrayList<>();
        if (user.getFriends() != null) {
            Set<Integer> userFriends = userStorage.findUserById(id).getFriends();
            Set<Integer> friendFriends = userStorage.findUserById(otherId).getFriends();
            commonFriends.addAll(userFriends.stream().map(userStorage::findUserById).collect(Collectors.toList()));
            commonFriends.retainAll(friendFriends.stream().map(userStorage::findUserById).collect(Collectors.toList()));
        }
        return commonFriends;
    }

    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0) {
            throw new UserNotFoundException("Номер пользователя не может быть отрицательным.");
        }
        userStorage.findUserById(id).getFriends().add(friendId);
        userStorage.findUserById(friendId).getFriends().add(id);
    }

    public void removeFriend(int id, int friendId) {
        userStorage.findUserById(id).getFriends().remove(friendId);
        userStorage.findUserById(friendId).getFriends().remove(id);
        userStorage.findUserById(id).getRequest().remove(friendId);
        userStorage.findUserById(friendId).getRequest().remove(id);
    }

    public void sendRequest(int id, int friendId) {
        userStorage.findUserById(id).getRequest().put(friendId,Status.PENDING);
        userStorage.findUserById(friendId).getRequest().put(id,Status.PENDING);
    }

    public List<Integer> checkRequest(int id) {
        List<Integer> pendingFriends = new ArrayList<>();
        for (Map.Entry<Integer,Status> entry : userStorage.findUserById(id).getRequest().entrySet()) {
            if (entry.getValue().equals(Status.PENDING)) {
                pendingFriends.add(entry.getKey());
            }
        }
        return pendingFriends;
    }

    public void acceptRequest(int id, int friendId) {
        userStorage.findUserById(id).getRequest().put(friendId,Status.ACCEPTED);
        userStorage.findUserById(friendId).getRequest().put(id,Status.ACCEPTED);
        addFriend(id,friendId);
    }

    public void declineRequest(int id, int friendId) {
        userStorage.findUserById(id).getRequest().put(friendId,Status.DECLINE);
        userStorage.findUserById(friendId).getRequest().put(id,Status.DECLINE);
    }
}
