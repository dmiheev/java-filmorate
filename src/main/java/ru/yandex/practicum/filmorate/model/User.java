package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    @EqualsAndHashCode.Exclude
    private int id;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}