package ru.yandex.practicum.filmorate.model;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@lombok.Data
@lombok.Builder
@Validated
public class Film {
    int id;
    @NotBlank(message = "Имя фильма не должно быть пустым.")
    String name;
    @Size(max = 200, message = "Максимальная длинна описания 200 символов.")
    String description;
    @ReleaseDate(value = "1895-12-28", message = "Дата релиза не может быть раньше 28 декабря 1895 года.")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной.")
    Long duration;
}