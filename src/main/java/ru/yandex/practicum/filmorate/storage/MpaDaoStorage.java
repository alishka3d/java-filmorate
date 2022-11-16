package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDaoStorage {

    Mpa getMpaById(Integer mpaId);

    List<Mpa> getAllMpa();
}
