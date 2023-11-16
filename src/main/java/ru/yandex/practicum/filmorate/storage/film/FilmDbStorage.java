package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT films.id, films.name, films.description, films.duration, films.release_date, " +
                "films.mpa_rating_id as mpa_id, mpa.name as mpa_name, " +
                "genres.id as genre_id, genres.name as genre_name FROM films " +
                "LEFT JOIN mpa_ratings mpa on films.mpa_rating_id = mpa.id " +
                "LEFT JOIN film_genres on films.id = film_genres.film_id " +
                "LEFT JOIN genres on film_genres.genre_id = genres.id " +
                "ORDER BY films.id";

        return getCompleteFilmFromQuery(sql);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();

        return getFilmById(filmId);
    }

    @Override
    public Film update(Film item) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa_rating_id = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                item.getName(),
                item.getDescription(),
                item.getReleaseDate(),
                item.getDuration(),
                item.getMpa().getId(),
                item.getId());

        return getFilmById(item.getId());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        String sql = "SELECT films.id, films.name, films.description, films.duration, films.release_date, " +
                "films.mpa_rating_id as mpa_id, mpa.name as mpa_name, " +
                "genres.id as genre_id, genres.name as genre_name from films " +
                "LEFT JOIN mpa_ratings mpa on films.mpa_rating_id = mpa.id " +
                "LEFT JOIN film_genres on films.id = film_genres.film_id " +
                "LEFT JOIN genres on film_genres.genre_id = genres.id " +
                "WHERE films.id = ? ORDER BY genre_id";

        List<Film> films = getCompleteFilmFromQuery(sql, filmId);

        return films.get(0);
    }

    @Override
    public Film delete(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Film film = getFilmById(filmId);
        String sqlQuery = "DELETE FROM films WHERE id = ? ";
        if (jdbcTemplate.update(sqlQuery, filmId) == 0) {
            throw new DataNotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        return film;
    }

    public List<Film> getPopular(Integer count) {

        String sql = "SELECT films.id, films.name, films.description, films.duration, films.release_date, " +
                "films.mpa_rating_id as mpa_id, mpa.name as mpa_name, " +
                "genres.id as genre_id, genres.name as genre_name FROM films " +
                "LEFT JOIN mpa_ratings mpa on films.mpa_rating_id = mpa.id " +
                "LEFT JOIN film_genres on films.id = film_genres.film_id " +
                "LEFT JOIN genres on film_genres.genre_id = genres.id " +
                "LEFT JOIN film_likes on films.id = film_likes.film_id " +
                "GROUP BY films.id, genre_id " +
                "ORDER BY COUNT(film_likes.user_id) DESC LIMIT ?";

        return getCompleteFilmFromQuery(sql, count);
    }

    private Genre toGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    private Mpa toMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

    private Film toFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .genres(new HashSet<>())
                .build();
    }

    private List<Film> getCompleteFilmFromQuery(String sql, Object... params) {
        return jdbcTemplate.query(sql,
                rs -> {
                    Map<Long, Film> map = new HashMap<>();
                    while (rs.next()) {
                        if (!map.containsKey(rs.getLong("id"))) {
                            Film film = toFilm(rs);

                            if (rs.getLong("mpa_id") != 0) {
                                film.setMpa(toMpa(rs));
                            }

                            if (rs.getLong("genre_id") != 0) {
                                film.getGenres().add(toGenre(rs));
                            }

                            map.put(film.getId(), film);
                        } else {
                            Film film = map.get(rs.getLong("id"));
                            if (rs.getLong("genre_id") != 0) {
                                film.getGenres().add(toGenre(rs));
                            }
                        }
                    }
                    return map.isEmpty() ? new ArrayList<>() : new ArrayList<>(map.values());
                }, params
        );
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT CASE WHEN COUNT(id) > 0 THEN true ELSE false END FROM films WHERE id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }
}