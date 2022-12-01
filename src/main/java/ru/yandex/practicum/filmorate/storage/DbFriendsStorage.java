package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DbFriendsStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public void addFriend(int userId, int friendId) {
        if (userStorage.getUsers().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new UserNotFoundException("Идентификатор пользователя не найден");
        } else if (userStorage.getUsers().stream().noneMatch(u -> Objects.equals(u.getId(), friendId))) {
            throw new UserNotFoundException("Идентификатор друга не найден");
        }
        String sql =
                "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) " +
                        "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getAllFriendsUser(int id) {
        String sql =
                "SELECT FRIEND_ID " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID = ?";
        List<Integer> friendsUser = jdbcTemplate.queryForList(sql, Integer.class, id);
        log.info("Все друзья пользователя с id {}:", id);
        return friendsUser.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (userStorage.getUsers().stream().noneMatch(u -> Objects.equals(u.getId(), userId))) {
            throw new UserNotFoundException("Идентификатор пользователя не найден");
        } else if (userStorage.getUsers().stream().noneMatch(u -> Objects.equals(u.getId(), friendId))) {
            throw new UserNotFoundException("Идентификатор друга не найден");
        }
        String sql =
                "DELETE " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID = ? " +
                        "AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }
}