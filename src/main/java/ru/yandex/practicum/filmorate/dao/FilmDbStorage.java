package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inteface.FilmStorage;

import java.util.*;

@Repository
@Qualifier
public class FilmDbStorage implements FilmStorage {
    private static int filmId = 0;
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM Film";
        List<Film> films = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        while (rs.next()) {
            Set<Genre> uniqueGenre = new HashSet<>();
            String sqlGenre = "SELECT genre_id FROM FilmGenre WHERE film_id=?";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGenre, rs.getInt("film_id"));
            while (rowSet.next()) {
                uniqueGenre.add(genreStorage.findGenreById(rowSet.getInt("genre_id")));
            }
            List<Genre> resultGenre = new ArrayList<>(uniqueGenre);
            resultGenre.sort(Comparator.comparingInt(Genre::getId));
            films.add(Film.builder()
                    .id(rs.getInt("film_id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate())
                    .duration(rs.getLong("duration"))
                    .rate(rs.getInt("rate"))
                    .genres(resultGenre)
                    .mpa(mpaStorage.findMpaById(rs.getInt("age_id")))
                    .build());
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        filmId++;
        film.setId(filmId);
        String sql1 =
                "INSERT INTO Film VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql1, film.getId(), film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId());
        film.setMpa(mpaStorage.findMpaById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            String sql = "INSERT INTO FilmGenre VALUES (?, ?)";
            film.getGenres().forEach(genre -> jdbcTemplate.update(sql, film.getId(), genreStorage.findGenreById(genre.getId()).getId()));
            Set<Genre> uniqueGenre = new HashSet<>();
            film.getGenres().forEach(genre1 -> uniqueGenre.add(genreStorage.findGenreById(genre1.getId())));
            List<Genre> resultGenre = new ArrayList<>(uniqueGenre);
            film.setGenres(resultGenre);
        } else {
            film.setGenres(new ArrayList<>());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (findFilmById(film.getId()) == null || film.getId() > filmId) {
            throw new FilmNotFoundException("Фильм не найден.");
        } else {
            Film oldFilm = findFilmById(film.getId());
            if (!film.getName().equals(oldFilm.getName())) {
                jdbcTemplate.update("UPDATE Film SET name=? WHERE film_id=?", film.getName(), film.getId());
                film.setName(film.getName());
            }
            if (!film.getDescription().equals(oldFilm.getDescription())) {
                jdbcTemplate.update("UPDATE Film SET description=? WHERE film_id=?", film.getDescription(), film.getId());
                film.setDescription(film.getDescription());
            }
            if (!film.getReleaseDate().equals(oldFilm.getReleaseDate())) {
                jdbcTemplate.update("UPDATE Film SET release_date=? WHERE film_id=?", film.getReleaseDate(), film.getId());
                film.setReleaseDate(film.getReleaseDate());
            }
            if (!film.getDuration().equals(oldFilm.getDuration())) {
                jdbcTemplate.update("UPDATE Film SET duration=? WHERE film_id=?", film.getDuration(), film.getId());
                film.setDuration(film.getDuration());
            }
            if (film.getRate() != oldFilm.getRate()) {
                jdbcTemplate.update("UPDATE Film SET rate=? WHERE film_id=?", film.getRate(), film.getId());
                film.setRate(film.getRate());
            }
            if (film.getGenres() != null) {
                jdbcTemplate.update("DELETE FROM FilmGenre WHERE film_id=?", film.getId());
                Set<Integer> uniqueList = new HashSet<>();
                film.getGenres().forEach(g -> uniqueList.add(g.getId()));
                List<Genre> resultGenre = new ArrayList<>();
                uniqueList.forEach(gId -> resultGenre.add(genreStorage.findGenreById(gId)));
                String sql = "INSERT INTO FilmGenre VALUES (?, ?)";
                resultGenre.forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));
                resultGenre.sort(Comparator.comparingInt(Genre::getId));
                film.setGenres(resultGenre);
            } else {
                film.setGenres(new ArrayList<>());
            }
            if (film.getMpa() != null && film.getMpa().getId() != oldFilm.getMpa().getId()) {
                jdbcTemplate.update("UPDATE Film SET age_id=? WHERE film_id=?", mpaStorage.findMpaById(film.getMpa().getId()).getId(), film.getId());
                film.setMpa(mpaStorage.findMpaById(film.getMpa().getId()));
            }
        }
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        if (id > filmId) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        String sql = "SELECT * FROM Film WHERE film_id=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        rs.next();
        Set<Genre> uniqueGenre = new HashSet<>();
        String sqlGenre = "SELECT genre_id FROM FilmGenre WHERE film_id=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGenre, id);
        while (rowSet.next()) {
            uniqueGenre.add(genreStorage.findGenreById(rowSet.getInt("genre_id")));
        }
        List<Genre> resultGenre = new ArrayList<>(uniqueGenre);
        resultGenre.sort(Comparator.comparingInt(Genre::getId));
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate())
                .duration(rs.getLong("duration"))
                .rate(rs.getInt("rate"))
                .genres(resultGenre)
                .mpa(mpaStorage.findMpaById(rs.getInt("age_id")))
                .build();
    }
}
