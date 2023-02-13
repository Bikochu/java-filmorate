package ru.yandex.practicum.filmorate.model;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@lombok.Data
@lombok.Builder
public class Film {
    int id;
    @NotNull(message = "Имя фильма не должно быть пустым.")
    String name;
    @Size(max = 200, message = "Максимальная длинна описания 200 символов.")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной.")
    Long duration;
}