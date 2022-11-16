package ru.yandex.practicum.filmorate.storage;

public interface LikeDaoStorage {

    void putLike(int id, int userId);

    void removeLikes(int id, int userId);
}
