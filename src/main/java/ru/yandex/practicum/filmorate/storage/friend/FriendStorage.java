package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    static User toUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException(String.format("Невозможно добавить в друзья самого себя для пользователя с" +
                    " id = %d", userId));
        }
        if (exists(userId, friendId)) {
            throw new DataAlreadyExistException(String.format("У пользователя с id = %d уже есть друг " +
                    "с id = %d", userId, friendId));
        }
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!exists(userId, friendId)) {
            throw new DataNotFoundException(String.format("У пользователя с id = %d отсутствует друг" +
                    " с id = %d", userId, friendId));
        }
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        String sql = "SELECT id, email, login, name, birthday FROM users LEFT JOIN friends on id = friend_id " +
                "WHERE user_id = ? ORDER BY id";

        return jdbcTemplate.query(sql, FriendStorage::toUser, userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        String sql = "SELECT id, name, email, login, birthday FROM users LEFT JOIN friends on id = friend_id " +
                "WHERE user_id = ? INTERSECT " +
                "SELECT id, name, email, login, birthday FROM users LEFT JOIN friends on id = friend_id " +
                "WHERE user_id = ? ORDER BY id";

        return jdbcTemplate.query(sql, FriendStorage::toUser, userId, friendId);
    }

    public boolean exists(Long userId, Long friendId) {
        String sql = "SELECT CASE WHEN COUNT(user_id) > 0 THEN true ELSE false END " +
                "FROM friends WHERE user_id = ? AND friend_id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, userId, friendId);

        return exists != null && exists;
    }
}