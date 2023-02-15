package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validCreateUser() {
        User user = User.builder()
                .email("practicum@mail.ru")
                .login("login")
                .name("Practicum")
                .birthday(LocalDate.of(1987, 12, 20))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(0, violations.size(), "Пользователь не добавлен.");
    }

    @Test
    void validEmailOfUser() {
        User user = User.builder()
                .email("practicumMail.ru")
                .login("login")
                .name("Practicum")
                .birthday(LocalDate.of(1987,12,20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Е-маил должен содержать символ @", violations.iterator().next().getMessage());
    }

    @Test
    void validLoginOfUser() {
        User user = User.builder()
                .email("practicum@mail.ru")
                .name("Practicum")
                .birthday(LocalDate.of(1987,12,20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым и содержать пробелы.", violations.iterator().next().getMessage());
    }

    @Test
    void validNameOfUser() {
        User user = User.builder()
                .email("practicum@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1987,12,20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(0, violations.size(), "Пользователь не добавлен.");
    }

    @Test
    void validBirthdayOfUser() {
        User user = User.builder()
                .email("practicum@mail.ru")
                .login("login")
                .name("Practicum")
                .birthday(LocalDate.of(3000,12,20))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем.", violations.iterator().next().getMessage());
    }
}