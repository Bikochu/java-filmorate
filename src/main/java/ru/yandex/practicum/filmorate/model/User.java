package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

@lombok.Data
@lombok.Builder
@Validated
public class User {
    int id;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Е-маил должен содержать символ @")
    String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    String login;
    @Getter(AccessLevel.NONE)
    String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    LocalDate birthday;
    final Set<Integer> filmsLike = new HashSet<>();

    public String getName() {
        if (name == null || name.isBlank()) {
            return login;
        } else {
            return name;
        }
    }
}