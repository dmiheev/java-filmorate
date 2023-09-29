package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int maxId = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        int nextValId = ++maxId;
        user = user.toBuilder().id(nextValId).build();

        validateUser(user);

        users.put(nextValId, user);
        log.debug("Создан пользователь {}", user);

        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validateUser(user);

        users.put(user.getId(), user);
        log.debug("Обновлен пользователь {}", user);

        return user;
    }

    private void validateUser(User user) {
        if (users.containsValue(user)) {
            throw new ValidationException("Такой пользователь уже создан.");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин содержит пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    private int generateId() {
        return ++maxId;
    }
}