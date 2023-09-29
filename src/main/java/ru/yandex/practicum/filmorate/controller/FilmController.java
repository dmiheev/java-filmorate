package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int maxId = 0;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        int nextValId = ++maxId;
        film = film.toBuilder().id(nextValId).build();

        validateFilm(film);

        films.put(nextValId, film);
        log.debug("Создан фильм {}", film);

        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validateFilm(film);

        films.put(film.getId(), film);
        log.debug("Обновлен фильм {}", film);

        return film;
    }

    private void validateFilm(Film film) {
        if (films.containsValue(film)) {
            throw new ValidationException("Такой фильм уже создан.");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
    }
}
