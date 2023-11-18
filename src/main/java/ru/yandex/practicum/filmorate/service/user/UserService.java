package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User create(User user) {
        isValidUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        isValidUser(user);
        return userStorage.update(user);
    }

    public User delete(Long id) {
        exists(id);
        userStorage.delete(id);
        return userStorage.getUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        exists(id);
        return userStorage.getUserById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья!");
        }
        exists(userId);
        exists(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей!");
        }
        exists(userId);
        exists(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        exists(userId);
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        exists(firstUserId);
        exists(secondUserId);

        return friendStorage.getCommonFriends(firstUserId, secondUserId);
    }

    private void isValidUser(User user) {
        exists(user.getId());
    }

    public void exists(Long id) {
        if (id != null && !userStorage.exists(id)) {
            throw new DataNotFoundException(String.format("Не найден пользователь по id = %d.", id));
        }
    }

}
