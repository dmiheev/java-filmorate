package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Film extends StorageData {
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания для фильма — 200 символов.")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной.")
    private int duration;

    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();

    @JsonIgnore
    private long rate = 0;

    public void addLike(long userId) {
        userIds.add(userId);
        rate = userIds.size();
    }

    public void removeLike(long userId) {
        userIds.remove(userId);
        rate = userIds.size();
    }
}
