package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inteface.UserStorage;

import java.util.*;

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
        return userStorage.getFriends(id);
        /*Set<Integer> usersFriends = userDbStorage.findUserById(id).getFriends();
        return usersFriends.stream()
                .map(userDbStorage::findUserById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());*/
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
        /*User user = userDbStorage.findUserById(id);
        List<User> commonFriends = new ArrayList<>();
        if (user.getFriends() != null) {
            Set<Integer> userFriends = userDbStorage.findUserById(id).getFriends();
            Set<Integer> friendFriends = userDbStorage.findUserById(otherId).getFriends();
            commonFriends.addAll(userFriends.stream().map(userDbStorage::findUserById).collect(Collectors.toList()));
            commonFriends.retainAll(friendFriends.stream().map(userDbStorage::findUserById).collect(Collectors.toList()));
        }
        return commonFriends;*/
    }

    public void addFriend(int id, int friendId) {
        userStorage.addFriend(id,friendId);
        /*if (id < 0 || friendId < 0) {
            throw new UserNotFoundException("Номер пользователя не может быть отрицательным.");
        }
        userDbStorage.findUserById(id).getFriends().add(friendId);
        userDbStorage.findUserById(friendId).getFriends().add(id);*/
    }

    public void removeFriend(int id, int friendId) {
        userStorage.removeFriend(id, friendId);
        /*userDbStorage.findUserById(id).getFriends().remove(friendId);
        userDbStorage.findUserById(friendId).getFriends().remove(id);
        userDbStorage.findUserById(id).getRequest().remove(friendId);
        userDbStorage.findUserById(friendId).getRequest().remove(id);*/
    }

    /*public void sendRequest(int id, int friendId) {
        userDbStorage.findUserById(id).getRequest().put(friendId,Status.PENDING);
        userDbStorage.findUserById(friendId).getRequest().put(id,Status.PENDING);
    }

    public List<Integer> checkRequest(int id) {
        List<Integer> pendingFriends = new ArrayList<>();
        for (Map.Entry<Integer,Status> entry : userDbStorage.findUserById(id).getRequest().entrySet()) {
            if (entry.getValue().equals(Status.PENDING)) {
                pendingFriends.add(entry.getKey());
            }
        }
        return pendingFriends;
    }

    public void acceptRequest(int id, int friendId) {
        userDbStorage.findUserById(id).getRequest().put(friendId,Status.ACCEPTED);
        userDbStorage.findUserById(friendId).getRequest().put(id,Status.ACCEPTED);
        addFriend(id,friendId);
    }

    public void declineRequest(int id, int friendId) {
        userDbStorage.findUserById(id).getRequest().put(friendId,Status.DECLINE);
        userDbStorage.findUserById(friendId).getRequest().put(id,Status.DECLINE);
    }*/
}
