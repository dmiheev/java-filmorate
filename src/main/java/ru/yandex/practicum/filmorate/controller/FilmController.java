package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @GetMapping()
    public List<Film> getAllFilms() {
        final List<Film> films = service.getAll();
        log.info("Get all films {}", films.size());
        return films;
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody final Film film) {
        log.info("Получен запрос к эндпоинту: POST /film, тело запроса: '{}'", film);
        return service.create(film);
    }

    @PutMapping()
    public Film setFilm(@Validated(Update.class) @RequestBody final Film film) {
        log.info("Получен запрос к эндпоинту: PUT /film, тело запроса: '{}'", film);
        return service.update(film);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.info("Get film id={}", id);
        return service.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Add like film id={} from user={}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Remove like film id={} from user={}", id, userId);
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Get popular films count = {}", count);
        return service.getPopular(count);
    }
}
