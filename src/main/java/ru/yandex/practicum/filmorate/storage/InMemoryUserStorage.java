package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getUser() {
        return List.copyOf(users.values());
    }

    @Override
    public User createUser(User user) {
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
            log.info("создан новый пользователь");
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            if (user.getName().isBlank()) {
                log.debug("Имя не указано. В качестве имени используется логин.");
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("пользователь успешно обновлён");
            return user;
        } else {
            log.error("Пользователь не найден.");
            throw new UserNotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public User findById(int id) {
        if (users.containsKey(id)) {
            log.info("пользователь с id {} найден", id);
            return users.get(id);
        } else {
            log.error("Пользователь с id {} не найден", id);
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    private void validateUser(User user) {
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
