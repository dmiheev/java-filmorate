package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void addFilmWithWrongReleaseDate() {
        Film film = Film.builder()
                .name("Терминатор")
                .description("Терминатор")
                .releaseDate(FilmController.MIN_RELEASE_DATE.minusDays(1))
                .duration(120)
                .build();
        ValidationException ex = assertThrows(
                ValidationException.class, () -> filmController.create(film)
        );
    }

}
