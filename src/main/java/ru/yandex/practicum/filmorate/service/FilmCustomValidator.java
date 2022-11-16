package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;


@Slf4j
@Component
public class FilmCustomValidator {

    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);

    public boolean isValid(Film film) {
        if (film.getReleaseDate().isBefore(DATE_RELEASE)) {
            log.error("Дата релиза фильма не может быть раньше " + DATE_RELEASE);
            throw new ValidationException("Дата релиза фильма не может быть раньше " + DATE_RELEASE);
        } else if (film.getDescription().length() > 200) {
            log.error("Длина описания фильма не может быть больше 200 символов");
            throw new ValidationException("Длина описания фильма не может быть больше 200 символов");
        } else if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        } else if (film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        } else {
            return true;
        }
    }
}