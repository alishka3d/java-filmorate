package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public List<User> findAll() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User findById(int id) {
        User user = userStorage.findById(id);
        log.info("По id {} найден пользователь {}", id, user.getLogin());
        return user;
    }

    public void removeUser(User user) {
        userStorage.deleteUser(user);
    }

    public void addFriend(int id, int friendId) {
        if (!userStorage.getUsers().contains(userStorage.findById(id)) || !userStorage.getUsers().contains(userStorage.findById(friendId))) {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        } else {
            friendsStorage.addFriend(id, friendId);
            log.info("Пользователь {} добавил в друзья пользователя {}.", userStorage.findById(id).getLogin(), userStorage.findById(friendId).getLogin());
        }
    }

    public void removeFriend(int id, int friendId) {
        if (friendsStorage.getAllFriendsUser(id).contains(userStorage.findById(friendId))) {
            log.info("Пользователь {} удалил из друзей пользователя {}.", userStorage.findById(id).getLogin(), userStorage.findById(friendId).getLogin());
            friendsStorage.deleteFriend(id, friendId);
        } else {
            log.error("Такого пользователя нет в друзьях");
            throw new UserNotFoundException("Такого пользователя нет в друзьях");
        }
    }

    public List<User> getAllFriends(int id) {
        return friendsStorage.getAllFriendsUser(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> user = getAllFriends(id);
        List<User> otherUser = getAllFriends(otherId);
        log.info("Общие друзья у пользователей с id {} и {}:", id, otherId);
        return user.stream()
                .filter(otherUser::contains)
                .collect(Collectors.toList());
    }
}