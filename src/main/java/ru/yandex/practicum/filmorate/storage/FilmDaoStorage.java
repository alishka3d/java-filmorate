package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDaoStorage {

    List<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film findById(int id);

    void deleteFilm(Film film);

    List<Film> popularFilms(Integer count);
}