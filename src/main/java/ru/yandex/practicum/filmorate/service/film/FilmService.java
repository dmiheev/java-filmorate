package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public Film create(Film film) {
        isValidFilm(film);
        Film createdFilm = filmStorage.create(film);
        film.setId(createdFilm.getId());
        genreStorage.add(film);
        return getFilmById(createdFilm.getId());
    }

    public Film update(Film film) {
        isValidFilm(film);
        genreStorage.delete(film);
        genreStorage.add(film);
        return filmStorage.update(film);
    }

    public Film delete(Long id) {
        exists(id);
        filmStorage.delete(id);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        exists(id);
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        exists(filmId);
        checkUserExists(userId);

        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (likeStorage.getLikes(filmId) != null) {
                likeStorage.deleteLike(filmId, userId);
            } else {
                throw new DataNotFoundException("Лайк от пользователя c ID=" + userId + " не найден!");
            }
        } else {
            throw new DataNotFoundException("Фильм c ID=" + filmId + " не найден!");
        }
    }

    public List<Film> getPopular(Integer count) {
        if (count < 1) {
            throw new ValidationException("Количество фильмов для вывода не должно быть меньше 1");
        }
        return filmStorage.getPopular(count);
    }

    public void putGenres(Film film) {
        genreStorage.delete(film);
        genreStorage.add(film);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(genreStorage.getFilmGenres(filmId));
    }

    private void isValidFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза фильма: " + film.getReleaseDate());
        }

        exists(film.getId());
        checkMpaExists(film.getMpa().getId());
    }

    public void exists(Long id) {
        if (id != null && !filmStorage.exists(id)) {
            throw new DataNotFoundException(String.format("Не найден фильм по id = %d.", id));
        }
    }

    private void checkMpaExists(Integer mpaId) {
        if (mpaId != null && !mpaStorage.exists(mpaId)) {
            throw new DataNotFoundException(String.format("Не найден рейтинг MPA по id = %d.", mpaId));
        }
    }

    private void checkUserExists(Long userId) {
        if (userId != null && !userStorage.exists(userId)) {
            throw new DataNotFoundException(String.format("Не найден пользователь по id = %d.", userId));
        }
    }
}
