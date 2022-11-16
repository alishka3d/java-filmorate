package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsDaoStorage;
import ru.yandex.practicum.filmorate.storage.UserDaoStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDaoStorage userDaoStorage;
    private final FriendsDaoStorage friendsDaoStorage;


    public List<User> findAll() {
        return userDaoStorage.getUsers();
    }

    public User createUser(User user) {
        return userDaoStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userDaoStorage.updateUser(user);
    }

    public User findById(int id) {
        User user = userDaoStorage.findById(id);
        log.info("По id {} найден пользователь {}", id, user.getLogin());
        return user;
    }

    public void removeUser(User user) {
        userDaoStorage.deleteUser(user);
    }

    public void addFriend(int id, int friendId) {
        if (!userDaoStorage.getUsers().contains(userDaoStorage.findById(id)) || !userDaoStorage.getUsers().contains(userDaoStorage.findById(friendId))) {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        } else {
            friendsDaoStorage.addFriend(id, friendId);
            log.info("Пользователь {} добавил в друзья пользователя {}.", userDaoStorage.findById(id).getLogin(), userDaoStorage.findById(friendId).getLogin());
        }
    }

    public void removeFriend(int id, int friendId) {
        if (friendsDaoStorage.getAllFriendsUser(id).contains(userDaoStorage.findById(friendId))) {
            log.info("Пользователь {} удалил из друзей пользователя {}.", userDaoStorage.findById(id).getLogin(), userDaoStorage.findById(friendId).getLogin());
            friendsDaoStorage.deleteFriend(id, friendId);
        } else {
            log.error("Такого пользователя нет в друзьях");
            throw new UserNotFoundException("Такого пользователя нет в друзьях");
        }
    }

    public List<User> getAllFriends(int id) {
        return friendsDaoStorage.getAllFriendsUser(id);
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