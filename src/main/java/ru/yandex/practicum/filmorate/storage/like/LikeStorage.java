package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

import java.util.List;


@Component
@RequiredArgsConstructor
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Long filmId, Long userId) {
        if (exists(filmId, userId)) {
            throw new DataAlreadyExistException(String.format("Фильму с id = %d уже добавлен лайк от пользователя " +
                    "с id = %d", filmId, userId));
        }
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!exists(filmId, userId)) {
            throw new DataNotFoundException(String.format("У фильма с id = %d отсутствует лайк от пользователя" +
                    " с id = %d", filmId, userId));
        }
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }

    public boolean exists(Long filmId, Long userId) {
        String sql = "SELECT CASE WHEN COUNT(film_id) > 0 THEN true ELSE false END " +
                "FROM film_likes WHERE film_id = ? and user_id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, filmId, userId);

        return exists != null && exists;
    }
}