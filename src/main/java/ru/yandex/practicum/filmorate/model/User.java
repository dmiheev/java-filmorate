package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class User extends StorageData {
    @Email(message = "Электронная почта не соответствует формату.")
    @NotBlank(message = "Электронная почта должна быть заполнена.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    @JsonIgnore
    private transient Set<Long> friendIds = new HashSet<>();
}