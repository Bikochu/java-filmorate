package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FilmMapper implements RowMapper<Film> {

    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmMapper(MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genreList = new ArrayList<>();
        if (rs.getInt("genre_id") > 0) {
            genreList.add(genreStorage.findGenreById(rs.getInt("genre_id")));
        }
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .rate(rs.getInt("rate"))
                .genres(genreList)
                .mpa(mpaStorage.findMpaById(rs.getInt("age_id")))
                .build();
    }

}
