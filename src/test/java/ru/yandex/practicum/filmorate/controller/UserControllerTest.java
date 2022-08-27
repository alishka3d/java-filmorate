package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = User.builder()
                .id(1)
                .name("Heisenberg")
                .birthday(LocalDate.of(1971, 7, 4))
                .email("hzbrg@yahoo.com")
                .login("alishka3d")
                .build();
    }

    @Test
    void validateEmail() {
        user.setEmail("hzbrg.yahoo.com");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void validateLogin() {
        user.setLogin("ali ely");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void validateName() throws ValidationException {
        user.setName("");
        userController.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validateBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}