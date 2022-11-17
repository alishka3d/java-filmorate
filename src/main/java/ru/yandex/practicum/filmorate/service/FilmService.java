package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.FilmGenresDaoStorage;
import ru.yandex.practicum.filmorate.storage.GenreDaoStorage;
import ru.yandex.practicum.filmorate.storage.LikeDaoStorage;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmDaoStorage filmDaoStorage;
    private final LikeDaoStorage likeDaoStorage;
    private final GenreDaoStorage genreDaoStorage;
    private final FilmGenresDaoStorage filmGenresDaoStorage;

    private void loadData(Film film) {
        film.setGenres(genreDaoStorage.getGenresByFilm(film));
    }

    public List<Film> findAll() {
        List<Film> films = filmDaoStorage.getFilms();
        for (Film film : films) {
            loadData(film);
        }
        return films;
    }

    public Film createFilm(Film film) {
        Film newFilm = filmDaoStorage.createFilm(film);
        filmGenresDaoStorage.createGenreByFilm(newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        filmGenresDaoStorage.updateGenreFilm(film);
        filmGenresDaoStorage.createGenreByFilm(film);
        return filmDaoStorage.updateFilm(film);
    }

    public Film findById(int id) {
        Film film = filmDaoStorage.findById(id);
        loadData(film);
        log.info("По id {} найден фильм {}", id, film.getName());
        return film;
    }

    public void removeFilm(Film film) {
        filmDaoStorage.deleteFilm(film);
    }

    public void putLike(int id, int userId) {
        if (filmDaoStorage.getFilms().contains(filmDaoStorage.findById(id))) {
            log.info("Пользователь с id {} поставил лайк фильму {}.", userId, filmDaoStorage.findById(id).getName());
            likeDaoStorage.putLike(id, userId);
            filmDaoStorage.findById(id).getLikes().add(userId);
        } else {
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    public void removeLike(int id, int userId) {
        likeDaoStorage.removeLikes(id, userId);
    }

    public List<Film> popularFilms(int count) {
        if (count == 1) {
            log.info("Самый популярый фильм:");
        } else {
            log.info("{} популярных фильмов:", count);
        }
        return filmDaoStorage.popularFilms(count);
    }
}