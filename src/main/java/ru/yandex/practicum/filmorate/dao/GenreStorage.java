package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreStorage {

    List<Genre> genreList = new ArrayList<>();
    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        String sql = "SELECT * FROM Genre";
        genreList.addAll(jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name"))));
    }

    public Genre findGenreById(int id) {
        return genreList.stream().filter(genre -> genre.getId() == id).findFirst().orElse(null);
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
