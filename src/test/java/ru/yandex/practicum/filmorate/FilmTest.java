package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldHaveName() {
        Film film = Film.builder()
                .description("This is a description.")
                .releaseDate(LocalDate.of(2000,12,20))
                .duration(100L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Имя фильма не должно быть пустым.", violations.iterator().next().getMessage());
    }

    @Test
    public void testDescriptionValidation() {
        Film film = Film.builder()
                .name("Name")
                .description("01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "01234567890123456789012345678901234567890123456789012345678901234567890123456789")
                .releaseDate(LocalDate.of(2000,12,20))
                .duration(100L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Максимальная длинна описания 200 символов.", violations.iterator().next().getMessage());
    }

    @Test
    public void testDurationValidation() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000,12,20))
                .duration(-100L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Продолжительность фильма не может быть отрицательной.", violations.iterator().next().getMessage());
    }

    @Test
    void validAddFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000,12,20))
                .duration(100L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(0, violations.size(), "Фильм не добавлен.");
    }

    @Test
    void validReleaseDateOfFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1800,12,20))
                .duration(100L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", violations.iterator().next().getMessage());
    }
}