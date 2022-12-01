package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {

    List<User> getAllFriendsUser(int userId);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);
}