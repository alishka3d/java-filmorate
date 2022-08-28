package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<User> getUser() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.error("Пользователь {} уже существует.", user);
            throw new ValidationException("Пользователь" + user + "уже существует.");
        } else {
            validateUser(user);
            if (user.getName().isBlank()) {
                log.debug("Имя не указано. В качестве имени используется логин.");
                user.setName(user.getLogin());
            }
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("Создали пользователя: {}.", user);
            return user;
        }
    }

    @PutMapping
    public User updateFilm(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            if (user.getName().isBlank()) {
                log.debug("Имя не указано. В качестве имени используется логин.");
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользовател: {} - обновлен .", user);
            return user;
        } else {
            log.error("Пользователь не найден.");
            throw new ValidationException("Пользователь не найден.");
        }
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getId() < 0) {
            log.error("id не может быть отрицательным");
            throw new ValidationException("id не может быть отрицательным");
        } else if (user.getEmail().isBlank()) {
            log.error("Email не указан");
            throw new ValidationException("Email не указан");
        } else if (!user.getEmail().contains("@")) {
            log.error("Не верный формат Email");
            throw new ValidationException("Не верный формат Email");
        } else if (user.getLogin().isBlank()) {
            log.error("Логин не указан");
            throw new ValidationException("Логин не указан");
        } else if (user.getLogin().contains(" ")) {
            log.error("Логин не может содержать пробел");
            throw new ValidationException("Логин не может содержать пробел");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}