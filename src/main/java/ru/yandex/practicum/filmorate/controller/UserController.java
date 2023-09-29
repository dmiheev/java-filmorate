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
public class UserController extends AbstractController<User> {

    @GetMapping
    public Collection<User> getAll() {
        log.debug("Получение всех пользователей, текущее количество: {}", items.size());

        return super.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Создан пользователь {}", user);

        return super.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновлен пользователь {}", user);

        return super.update(user);
    }

    @Override
    protected void validate(User user) {
        super.validate(user);
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин содержит пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }
}