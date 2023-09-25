package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private int maxId = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        int nextValId = generateId();
        user.setId(nextValId);

        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());

        users.put(nextValId, user);
        log.debug("Создан пользователь {}", user);

        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {

        validateUser(user);
        if (user.getName().isBlank()) user.setName(user.getLogin());

        users.put(user.getId(), user);
        log.debug("Обновлен пользователь {}", user);

        return user;
    }

    private void validateUser(User user) {
        if (users.containsValue(user)) {
            throw new ValidationException("Такой фильм уже создан.");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private int generateId() {
        return ++maxId;
    }
}