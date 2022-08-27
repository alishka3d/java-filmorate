package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Getter
    private final LocalDate dateLow = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            log.error("Фильм: {} уже существует.", film.getName());
            throw new ValidationException("Фильм: " + film.getName() + " уже существует.");
        } else if (film.getId() < 0) {
            log.error("id не может быть отрицательным");
            throw new ValidationException("id не может быть отрицательным");
        } else if (film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Длина описания не должно превышать 200 символов");
            throw new ValidationException("Длина описания фильма не должно превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(dateLow)) {
            log.error("Дата релиза не может быть раньше " + dateLow);
            throw new ValidationException("Дата релиза не может быть раньше " + dateLow);
        } else if (film.getDuration() < 0) {
            log.error("Продолжительность должно быть положительным.");
            throw new ValidationException("Продолжительность должно быть положительным.");
        } else {
            film.setId(++id);
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}.", film);
            return film;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            if (film.getId() < 0) {
                log.error("id не может быть отрицательным");
                throw new ValidationException("id не может быть отрицательным");
            } else if (film.getName().isBlank()) {
                log.error("Название не может быть пустым");
                throw new ValidationException("Название не может быть пустым");
            } else if (film.getDescription().length() > 200) {
                log.error("Длина описания не должно превышать 200 символов");
                throw new ValidationException("Длина описания фильма не должно превышать 200 символов");
            } else if (film.getReleaseDate().isBefore(dateLow)) {
                log.error("Дата релиза не может быть раньше " + dateLow);
                throw new ValidationException("Дата релиза не может быть раньше " + dateLow);
            } else if (film.getDuration() < 0) {
                log.error("Продолжительность должно быть положительным.");
                throw new ValidationException("Продолжительность должно быть положительным.");
            }
            films.put(film.getId(), film);
            log.info("Фильм: {} обновлен.", film);
            return film;
        } else {
            log.error("Фильм не найден");
            throw new ValidationException("Фильм не найден");
        }
    }
}