package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inteface.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Qualifier
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static int userId = 0;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.query(sql, new UserMapper(jdbcTemplate));
    }

    @Override
    public User addUser(User user) {
        userId++;
        user.setId(userId);
        String sql = "INSERT INTO Users VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (findUserById(user.getId()) == null) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "UPDATE Users SET email=?, login=?, name=?, birthday=? WHERE user_id=?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User findUserById(int id) {
        if (id > userId) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            return User.builder()
                    .id(rs.getInt("user_id"))
                    .email(rs.getString("email"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .birthday(Objects.requireNonNull(rs.getDate("birthday")).toLocalDate())
                    .build();
        } else {
            throw new UserNotFoundException("Пользователь не найден.");
        }
    }

    public List<User> getFriends(int id) {
        String sql = "SELECT * FROM friendship WHERE user_id = ? AND status = 'ACCEPTED'";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(findUserById(rs.getInt("friend_id")));
        }
        return friends;
    }

    public void addFriend(int user_id, int friend_id) {
        if (findUserById(user_id) == null || user_id < 0 || findUserById(friend_id) == null || friend_id < 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        } else {
            String sql = "INSERT INTO Friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, user_id, friend_id, "ACCEPTED");
        }
    }

    public void removeFriend(int user_id, int friend_id) {
        if (findUserById(user_id) == null || user_id < 0 || findUserById(friend_id) == null || friend_id < 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        } else {
            String sql = "DELETE FROM Friendship WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sql, user_id, friend_id);
        }
    }

    public List<User> getCommonFriends(int user_id, int other_id) {
        List<User> commonFriends = new ArrayList<>(getFriends(user_id));
        commonFriends.retainAll(getFriends(other_id));
        return commonFriends;
    }

    public void addFilmsLike(int filmId, int userId) {
        String sql = "INSERT INTO Film_like (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
        String sqlRate = "UPDATE Film SET rate=? WHERE film_id=?";
        jdbcTemplate.update(sqlRate, +1, userId);
    }

    public void removeFilmLike(int filmId, int userId) {
        String sql = "DELETE FROM Film_like WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, filmId);
        String sqlRate = "UPDATE Film SET rate=? WHERE film_id=?";
        jdbcTemplate.update(sqlRate, -1, userId);
    }
}
