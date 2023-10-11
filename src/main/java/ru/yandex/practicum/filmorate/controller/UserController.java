package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping()
    public List<User> getAllUsers() {
        final List<User> users = service.getAll();
        log.info("Get all users {}", users.size());
        return users;
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody final User user) {
        log.info("Получен запрос к эндпоинту: POST /user, тело запроса: '{}'", user);
        return service.create(user);
    }

    @PutMapping()
    public User setUser(@Validated(Update.class) @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: PUT /user, тело запроса: '{}'", user);
        return service.update(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.info("Get user id={}", id);
        return service.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Add friend id={} to user={}", friendId, id);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Remove friend id={} from user={}", friendId, id);
        service.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Get friends from user={}", id);
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getOurFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Get our friends from user={} and other user={}", id, otherId);
        return service.getOurFriends(id, otherId);
    }
}
