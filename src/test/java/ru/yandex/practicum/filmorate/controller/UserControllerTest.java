package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    private final UserService userService;
    private final FriendsStorage friendsStorage;


    @Test
    @DisplayName("Create and find valid User")
    public void testFindUserById() {
        User userFind = new User();
        userFind.setEmail("pavelomsk95@mail.com");
        userFind.setLogin("Pavel95");
        userFind.setName("Pavel");
        userFind.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userFind);
        Optional<User> userOptional = Optional.ofNullable(userService.findById(userFind.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", userFind.getId())
                );
    }

    @Test
    @DisplayName("UpdateUser")
    public void testUpdateUser() {
        User userFind = new User();
        userFind.setEmail("pavelomsk95@mail.com");
        userFind.setLogin("Pavel95");
        userFind.setName("Pavel");
        userFind.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userFind);
        User updateUser = new User();
        updateUser.setId(userFind.getId());
        updateUser.setEmail("pavelomsk95@mail.com");
        updateUser.setLogin("Ivan95");
        updateUser.setName("Ivan");
        updateUser.setBirthday(LocalDate.of(1995, 10, 13));
        userService.updateUser(updateUser);
        assertEquals("Ivan", userService.findById(userFind.getId()).getName());
    }

    @Test
    @DisplayName("GetUsers")
    public void testGetUsers() {
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userOne);
        User userTwo = new User();
        userTwo.setEmail("ivanomsk95@mail.com");
        userTwo.setLogin("Ivan95");
        userTwo.setName("Ivan");
        userTwo.setBirthday(LocalDate.of(1993, 10, 13));
        userService.createUser(userTwo);
        assertEquals(2, userService.findAll().size());
    }

    @Test
    @DisplayName("RemoveUser")
    public void testRemoveUser() {
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userOne);
        User userTwo = new User();
        userTwo.setEmail("ivanomsk95@mail.com");
        userTwo.setLogin("Ivan95");
        userTwo.setName("Ivan");
        userTwo.setBirthday(LocalDate.of(1993, 10, 13));
        userService.createUser(userTwo);
        userService.removeUser(userOne);
        assertEquals(1, userService.findAll().size());
    }

    @Test
    @DisplayName("AddFriends")
    public void testAddFriends() {
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userOne);
        User userTwo = new User();
        userTwo.setEmail("ivanomsk95@mail.com");
        userTwo.setLogin("Ivan95");
        userTwo.setName("Ivan");
        userTwo.setBirthday(LocalDate.of(1993, 10, 13));
        userService.createUser(userTwo);
        friendsStorage.addFriend(userOne.getId(), userTwo.getId());
        assertEquals(1, friendsStorage.getAllFriendsUser(userOne.getId()).size());
    }

    @Test
    @DisplayName("DeleteFriends")
    public void testDeleteFriends() {
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userOne);
        User userTwo = new User();
        userTwo.setEmail("ivanomsk95@mail.com");
        userTwo.setLogin("Ivan95");
        userTwo.setName("Ivan");
        userTwo.setBirthday(LocalDate.of(1993, 10, 13));
        userService.createUser(userTwo);
        User userThree = new User();
        userThree.setEmail("pavel95@mail.com");
        userThree.setLogin("Pavel96");
        userThree.setName("Pavelomsk");
        userThree.setBirthday(LocalDate.of(1996, 10, 13));
        userService.createUser(userThree);
        friendsStorage.addFriend(userOne.getId(), userTwo.getId());
        friendsStorage.addFriend(userOne.getId(), userThree.getId());
        friendsStorage.deleteFriend(userOne.getId(), userTwo.getId());
        assertEquals(1, friendsStorage.getAllFriendsUser(userOne.getId()).size());
    }

    @Test
    @DisplayName("GetAllFriends")
    public void testGetAllFriends() {
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userService.createUser(userOne);
        User userTwo = new User();
        userTwo.setEmail("ivanomsk95@mail.com");
        userTwo.setLogin("Ivan95");
        userTwo.setName("Ivan");
        userTwo.setBirthday(LocalDate.of(1993, 10, 13));
        userService.createUser(userTwo);
        User userThree = new User();
        userThree.setEmail("pavel95@mail.com");
        userThree.setLogin("Pavel96");
        userThree.setName("Pavelomsk");
        userThree.setBirthday(LocalDate.of(1996, 10, 13));
        userService.createUser(userThree);
        friendsStorage.addFriend(userOne.getId(), userTwo.getId());
        friendsStorage.addFriend(userOne.getId(), userThree.getId());
        assertEquals(2, friendsStorage.getAllFriendsUser(userOne.getId()).size());
    }
}