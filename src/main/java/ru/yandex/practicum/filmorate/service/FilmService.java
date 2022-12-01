package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final FilmGenresStorage filmGenresStorage;

    private void loadData(Film film) {
        film.setGenres(genreStorage.getGenresByFilm(film));
    }

    public List<Film> findAll() {
        List<Film> films = filmStorage.getFilms();
        List<Genre> genres = genreStorage.getAllGenres();
        for (Film film : films) {
            loadData(film);
        }
        return films;
    }

    public Film createFilm(Film film) {
        Film newFilm = filmStorage.createFilm(film);
        filmGenresStorage.createGenreByFilm(newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        filmGenresStorage.updateGenreFilm(film);
        filmGenresStorage.createGenreByFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Film findById(int id) {
        Film film = filmStorage.findById(id);
        loadData(film);
        log.info("По id {} найден фильм {}", id, film.getName());
        return film;
    }

    public void removeFilm(Film film) {
        filmStorage.deleteFilm(film);
    }

    public void putLike(int id, int userId) {
        if (filmStorage.getFilms().contains(filmStorage.findById(id))) {
            log.info("Пользователь с id {} поставил лайк фильму {}.", userId, filmStorage.findById(id).getName());
            likeStorage.putLike(id, userId);
            filmStorage.findById(id).getLikes().add(userId);
        } else {
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    public void removeLike(int id, int userId) {
        likeStorage.removeLikes(id, userId);
    }

    public List<Film> popularFilms(int count) {
        if (count == 1) {
            log.info("Самый популярый фильм:");
        } else {
            log.info("{} популярных фильмов:", count);
        }
        return filmStorage.popularFilms(count);
    }
}