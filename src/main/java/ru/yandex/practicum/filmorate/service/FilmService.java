package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate).reversed();
    private final Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @Override
    protected void validate(Film data) {
        if (data.getName() == null || data.getName().isEmpty()) {
            throw new ValidationException("Film name invalid");
        }
        if (data.getReleaseDate() == null || data.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Film release date invalid");
        }
    }

    public void addLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.addLike(userId);
    }

    public void removeLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.removeLike(userId);
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }
}
