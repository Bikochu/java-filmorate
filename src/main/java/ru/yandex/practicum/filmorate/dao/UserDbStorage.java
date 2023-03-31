package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inteface.UserStorage;

import java.util.*;

@Repository
@Qualifier
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

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
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("Users").usingGeneratedKeyColumns("user_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("birthday", user.getBirthday());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        user.setId(key.intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlId = "SELECT user_id FROM Users ORDER BY user_id DESC LIMIT 1";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlId);
        sqlRowSet.next();
        if (user.getId() > sqlRowSet.getInt("user_id") || user.getId() <= 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "UPDATE Users SET email=?, login=?, name=?, birthday=? WHERE user_id=?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User findUserById(int id) {
        String sqlId = "SELECT user_id FROM Users ORDER BY user_id DESC LIMIT 1";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlId);
        sqlRowSet.next();
        if (id > sqlRowSet.getInt("user_id") || id <= 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        rs.next();
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Objects.requireNonNull(rs.getDate("birthday")).toLocalDate())
                .build();
    }

    @Override
    public List<User> getFriends(int id) {
        String sql = "SELECT * FROM friendship WHERE user_id = ? AND status = 'ACCEPTED'";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(findUserById(rs.getInt("friend_id")));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> commonFriends = new ArrayList<>(getFriends(userId));
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (findUserById(userId) == null || userId < 0 || findUserById(friendId) == null || friendId < 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "INSERT INTO Friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, "ACCEPTED");
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        if (findUserById(userId) == null || userId < 0 || findUserById(friendId) == null || friendId < 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "DELETE FROM Friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void addFilmsLike(int filmId, int userId) {
        String sql = "INSERT INTO Film_like (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
        String sqlRate = "UPDATE Film SET rate=? WHERE film_id=?";
        jdbcTemplate.update(sqlRate, +1, userId);
    }

    @Override
    public void removeFilmLike(int filmId, int userId) {
        String sql = "DELETE FROM Film_like WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, filmId);
        String sqlRate = "UPDATE Film SET rate=? WHERE film_id=?";
        jdbcTemplate.update(sqlRate, -1, userId);
    }
}
