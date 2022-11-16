package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDaoStorage {

    Genre getGenreById(int genreId);

    List<Genre> getAllGenres();

    Set<Genre> getGenresByFilm(Film film);
}