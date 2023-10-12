package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private final FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));

    @Test
    void addFilmWithWrongReleaseDate() {
        Film film = Film.builder()
                .name("Терминатор")
                .description("Терминатор")
                .releaseDate(FilmService.MIN_RELEASE_DATE.minusDays(1))
                .duration(120)
                .build();
        ValidationException ex = assertThrows(
                ValidationException.class, () -> filmController.addFilm(film)
        );
    }

}
