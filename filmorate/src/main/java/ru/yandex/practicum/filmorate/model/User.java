package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDate;

@lombok.Data
@lombok.Builder
public class User {
    int id;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Е-маил должен содержать символ @")
    String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    String login;
    @Getter (AccessLevel.NONE)
    String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    LocalDate birthday;

    public String getName() {
        if (name == null) {
            return login;
        } else {
            return name;
        }
    }
}