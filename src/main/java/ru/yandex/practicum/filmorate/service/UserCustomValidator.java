package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


@Slf4j
@Component
public class UserCustomValidator {

    public boolean isValid(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (!user.getEmail().contains("@")) {
            log.error("Email должен содержать @");
            throw new ValidationException("Email должен содержать @");
        } else if (user.getLogin().contains(" ")) {
            log.error("Логин не может содержать пробел");
            throw new ValidationException("Логин не может содержать пробел");
        } else if (user.getEmail().isBlank()) {
            log.error("Email не может быть пустым");
            throw new ValidationException("Email не может быть пустым");
        } else if (user.getLogin().isBlank()) {
            log.error("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        } else {
            return true;
        }
    }
}