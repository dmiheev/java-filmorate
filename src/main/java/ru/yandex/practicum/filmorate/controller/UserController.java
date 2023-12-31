package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET-запрос к эндпоинту: '/users' на получение списка пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{}' на получение пользователя с ID={}", id, id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{}/friends' на получение списка друзей пользователя с ID={}", id, id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{}/friends/common/{}' на получение списка общих друзей пользователя с ID={} и ID={}", id, otherId, id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен PUT-запрос к эндпоинту: '/users/{}/friends/{}' добавление в друзья пользователя с ID={} для пользователя с ID={}", id, friendId, friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/users/{}/friends/{}' удаление пользователя с ID={} из списка друзей пользователя с ID={}", id, friendId, friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос к эндпоинту: '/users' на добавление пользователя");
        user = userService.create(user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос к эндпоинту: '/users' на обновление пользователя с ID={}", user.getId());
        user = userService.update(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable Long id) {
        log.info("Получен DELETE-запрос к эндпоинту: '/users' на удаление пользователя с ID={}", id);
        return userService.delete(id);
    }
}
