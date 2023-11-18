package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    static Genre toGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sql, GenreStorage::toGenre);
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        exists(genreId);
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, GenreStorage::toGenre, genreId);

        return genres.get(0);
    }

    public void delete(Film film) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
    }

    public void add(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            final ArrayList<Genre> genreList = new ArrayList<>(film.getGenres());

            jdbcTemplate.batchUpdate(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, film.getId());
                            ps.setInt(2, genreList.get(i).getId());
                        }

                        public int getBatchSize() {
                            return genreList.size();
                        }
                    });
        }
    }

    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT genre_id, name FROM film_genres" +
                " INNER JOIN genres ON genre_id = id WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"), rs.getString("name")), filmId
        );
    }

    public boolean exists(Integer id) {
        String sql = "SELECT CASE WHEN COUNT(id) > 0 THEN true ELSE false END FROM genres WHERE id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);
        return exists != null && exists;
    }
}