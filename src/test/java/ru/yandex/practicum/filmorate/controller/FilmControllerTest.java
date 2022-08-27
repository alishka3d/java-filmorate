package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = Film.builder()
                .id(1)
                .name("Залечь на дно в Брюгге")
                .description("После того, как наемные убийцы Рэй и Кен запороли в Лондоне важное задание, " +
                        "их злобный шеф Гарри приказывает им отправиться в Брюгге и не высовываться. ")
                .releaseDate(LocalDate.of(2015, 5, 15))
                .duration(107)
                .build();
    }

    @Test
    void validateReleaseDate() {
        film.setReleaseDate(filmController.getDateLow().minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void validateDuration() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void validateName() {
        film.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void validateDescription() {
        film.setDescription("l".repeat(201));
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}