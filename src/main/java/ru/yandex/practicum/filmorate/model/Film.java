package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration,
                Set<Long> likes, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
    }

    @AssertFalse(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateLessMinPossibleDate() {
        return releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE);
    }

    public Set<Genre> getGenres() {
        return (genres == null) ? new HashSet<>() : genres;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_Date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_rating_id", mpa.getId());
        return values;
    }
}
