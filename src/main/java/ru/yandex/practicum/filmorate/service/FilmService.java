package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAll() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film findById(int id) {
        return filmStorage.findById(id);
    }

    public void putLike(int id, int userId) {
        filmStorage.findById(id).getLikes().add(userId);
    }

    public void removeLike(int id, int userId) {
        if (filmStorage.findById(id).getLikes().contains(userId)) {
            filmStorage.findById(id).getLikes().remove(userId);
        } else {
            throw new  UserNotFoundException("Пользователь с таким id не лайкал данный фильм");
        }
    }

    public List<Film> popularFilms(int count) {
        return filmStorage.getFilms()
                .stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count).collect(Collectors.toList());
    }

    private boolean isContains(int id, int userId) {
        if (filmStorage.findById(id).getLikes().contains(userId)) {
            if (filmStorage.findById(id).getLikes().contains(id)) {
                return true;
            } else {
                log.error("Фильм c id - {} не найден", id);
                throw new FilmNotFoundException("Фильм c id " + id + " не найден");
            }
        } else {
            log.error("Пользователь c id - {} не найден", id);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
    }
}
