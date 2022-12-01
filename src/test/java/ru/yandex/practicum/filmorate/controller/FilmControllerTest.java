package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    private final FilmService filmService;
    private final MpaStorage mpaStorage;
    private final UserStorage userStorage;

    @Test
    @DisplayName("Create and find valid Film")
    public void testFindFilmById() {
        Film filmFind = new Film();
        filmFind.setName("New FIlm");
        filmFind.setDescription("Description");
        filmFind.setReleaseDate(LocalDate.of(2000, 12, 12));
        filmFind.setDuration(90);
        filmFind.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(filmFind);
        Optional<Film> filmOptional = Optional.ofNullable(filmService.findById(filmFind.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", filmFind.getId())
                );
    }

    @Test
    @DisplayName("Update Film")
    public void testUpdateFilm() {
        Film film = new Film();
        film.setName("New FIlm");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDuration(90);
        film.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(film);
        Film updateFilm = new Film();
        updateFilm.setId(film.getId());
        updateFilm.setName("Update FIlm");
        updateFilm.setDescription("Update Description");
        updateFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        updateFilm.setDuration(90);
        updateFilm.setMpa(mpaStorage.getMpaById(1));
        filmService.updateFilm(updateFilm);
        assertEquals("Update FIlm", filmService.findById(film.getId()).getName());
    }

    @Test
    @DisplayName("GetFilms")
    public void testGetFilms() {
        Film oneFilm = new Film();
        oneFilm.setName("New FIlm");
        oneFilm.setDescription("Description");
        oneFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        oneFilm.setDuration(90);
        oneFilm.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(oneFilm);
        Film twoFilm = new Film();
        twoFilm.setName("Two FIlm");
        twoFilm.setDescription("Two Description");
        twoFilm.setReleaseDate(LocalDate.of(1998, 11, 9));
        twoFilm.setDuration(90);
        twoFilm.setMpa(mpaStorage.getMpaById(2));
        filmService.createFilm(twoFilm);
        assertEquals(2, filmService.findAll().size());
    }

    @Test
    @DisplayName("RemoveFilm")
    public void testRemoveFilm() {
        Film oneFilm = new Film();
        oneFilm.setName("New FIlm");
        oneFilm.setDescription("Description");
        oneFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        oneFilm.setDuration(90);
        oneFilm.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(oneFilm);
        Film twoFilm = new Film();
        twoFilm.setName("Two FIlm");
        twoFilm.setDescription("Two Description");
        twoFilm.setReleaseDate(LocalDate.of(1998, 11, 9));
        twoFilm.setDuration(90);
        twoFilm.setMpa(mpaStorage.getMpaById(2));
        filmService.createFilm(twoFilm);
        filmService.removeFilm(oneFilm);
        assertEquals(1, filmService.findAll().size());
    }

    @Test
    @DisplayName("PutLike")
    public void testPutLike() {
        Film oneFilm = new Film();
        oneFilm.setName("New FIlm");
        oneFilm.setDescription("Description");
        oneFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        oneFilm.setDuration(90);
        oneFilm.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(oneFilm);
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userStorage.createUser(userOne);
        filmService.putLike(oneFilm.getId(), userOne.getId());
        assertEquals(1, filmService.popularFilms(10).size());
    }

    @Test
    @DisplayName("RemoveLike")
    public void testRemoveLike() {
        Film oneFilm = new Film();
        oneFilm.setName("New FIlm");
        oneFilm.setDescription("Description");
        oneFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        oneFilm.setDuration(90);
        oneFilm.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(oneFilm);
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userStorage.createUser(userOne);
        User userTwo = new User();
        userTwo.setEmail("ivanomsk95@mail.com");
        userTwo.setLogin("Ivan95");
        userTwo.setName("Ivan");
        userTwo.setBirthday(LocalDate.of(1993, 10, 13));
        userStorage.createUser(userTwo);
        filmService.putLike(oneFilm.getId(), userOne.getId());
        filmService.putLike(oneFilm.getId(), userTwo.getId());
        filmService.removeLike(oneFilm.getId(), userOne.getId());
        assertEquals(1, filmService.popularFilms(10).size());
    }

    @Test
    @DisplayName("PopularFilms")
    public void testPopularFilms() {
        Film oneFilm = new Film();
        oneFilm.setName("New FIlm");
        oneFilm.setDescription("Description");
        oneFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        oneFilm.setDuration(90);
        oneFilm.setMpa(mpaStorage.getMpaById(1));
        filmService.createFilm(oneFilm);
        User userOne = new User();
        userOne.setEmail("pavelomsk95@mail.com");
        userOne.setLogin("Pavel95");
        userOne.setName("Pavel");
        userOne.setBirthday(LocalDate.of(1995, 10, 13));
        userStorage.createUser(userOne);
        Film twoFilm = new Film();
        twoFilm.setName("Two FIlm");
        twoFilm.setDescription("Two Description");
        twoFilm.setReleaseDate(LocalDate.of(1998, 11, 9));
        twoFilm.setDuration(90);
        twoFilm.setMpa(mpaStorage.getMpaById(2));
        filmService.createFilm(twoFilm);
        filmService.putLike(oneFilm.getId(), userOne.getId());
        filmService.putLike(twoFilm.getId(), userOne.getId());
        assertEquals(2, filmService.popularFilms(10).size());
    }
}