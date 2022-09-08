package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.getUser();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User findById(int id) {
        return userStorage.findById(id);
    }

    public void addFriend(int id, int friendId) {
        userStorage.findById(friendId);     // если пользователь не найден, то метод выбросит исключение
        userStorage.findById(id).getFriends().add(friendId);
        userStorage.findById(friendId).getFriends().add(id);
    }

    public void removeFriend(int id, int friendId) {
        userStorage.findById(friendId);
        userStorage.findById(id).getFriends().remove(friendId);
    }

    public List<User> getAllFriends(int id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.findById(id).getFriends()) {
            friends.add(userStorage.findById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int anotherId) {
        List<User> commonFriends = getAllFriends(id);
        commonFriends.retainAll(getAllFriends(anotherId));
        return commonFriends;
    }
}
