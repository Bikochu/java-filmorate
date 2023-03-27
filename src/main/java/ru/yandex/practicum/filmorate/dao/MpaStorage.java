package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Service
public class MpaStorage {
    List<Mpa> mpaList = new ArrayList<>();
    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        String sql = "SELECT * FROM Age_rating";
        mpaList.addAll(jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("age_id"),
                rs.getString("name"))));
    }

    public Mpa findMpaById(int id) {
        return mpaList.stream().filter(mpa -> mpa.getId() == id).findFirst().orElse(null);
    }

    public List<Mpa> getMpaList() {
        return mpaList;
    }
}
