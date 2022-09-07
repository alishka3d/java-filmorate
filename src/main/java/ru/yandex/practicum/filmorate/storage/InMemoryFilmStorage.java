package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Getter
    private final LocalDate dateLow = LocalDate.of(1895, 12, 28);

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Фильм: {} уже существует.", film.getName());
            throw new ValidationException("Фильм: " + film.getName() + " уже существует.");
        } else {
            validateFilm(film);
            film.setId(++id);
            films.put(film.getId(), film);
            return film;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Фильм: {} не найден", film.getName());
            throw new ValidationException("Фильм: " + film.getName() + " не найден");
        }
    }

    @Override
    public Film findById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            log.error("Фильм c id - {} не найден", id);
            throw new FilmNotFoundException("Фильм c id " + id + " не найден");
        }
    }

    private void validateFilm(Film film) throws ValidationException {
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
    }
}
