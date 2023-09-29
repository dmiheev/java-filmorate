package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(min = 1,max = 200, message = "Максимальная длина описания для фильма — 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной.")
    private int duration;
}
