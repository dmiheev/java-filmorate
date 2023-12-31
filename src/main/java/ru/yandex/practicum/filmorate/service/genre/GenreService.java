package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(Integer id) {
        exists(id);
        return genreStorage.getGenreById(id);
    }

    public void exists(Integer id) {
        if (id != null && !genreStorage.exists(id)) {
            throw new DataNotFoundException(String.format("Не найден жанр по id = %d.", id));
        }
    }

    private void validate(Genre genre) {
        if (genre == null) {
            throw new ValidationException("Передан пустой объект жанра.");
        }
        exists(genre.getId());
    }
}
