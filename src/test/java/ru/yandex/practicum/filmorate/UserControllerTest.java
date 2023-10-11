package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private final UserController userController = new UserController(new UserService(new InMemoryUserStorage()));


    @Test
    void addUserWithSpaceInLogin() {
        User user = User.builder()
                .email("dmitry@yandex.ru")
                .login("dmi try")
                .name("Дмитрий")
                .birthday(LocalDate.of(1979, 10, 5))
                .build();
        ValidationException ex = assertThrows(
                ValidationException.class, () -> userController.addUser(user)
        );
    }

}
