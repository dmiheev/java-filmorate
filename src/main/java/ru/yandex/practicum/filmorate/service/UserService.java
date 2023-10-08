package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractService<User> {

    @Autowired
    public UserService(Storage<User> storage) {
        this.storage = storage;
    }

    @Override
    protected void validate(User data) {
        if (data.getLogin() == null || data.getLogin().isEmpty() || data.getLogin().contains(" ")) {
            throw new ValidationException("User login invalid");
        }
        if (data.getName() == null || data.getName().isEmpty()) {
            data.setName(data.getLogin());
        }
    }

    public void addFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        storage.get(friendId);
        user.getFriendIds().add(friendId);
        friend.getFriendIds().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        storage.get(friendId);
        user.getFriendIds().remove(friendId);
        friend.getFriendIds().remove(userId);
    }

    public List<User> getFriends(long userId) {
        final User user = storage.get(userId);
        List<User> friends = new ArrayList<>();
        for (Long userFriend : user.getFriendIds()) {
            friends.add(storage.get(userFriend));
        }
        return friends;
    }

    public List<User> getOurFriends(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        List<User> friends = new ArrayList<>();
        for (Long userFriends : user.getFriendIds()) {
            for (Long friendFriends : friend.getFriendIds()) {
                if (friendFriends == userFriends) {
                    friends.add(storage.get(friendFriends));
                }
            }
        }
        return friends;
    }
}
