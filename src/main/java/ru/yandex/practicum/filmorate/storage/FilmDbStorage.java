package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmCustomValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmDaoStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmCustomValidator customValidator;

    @Override
    public List<Film> getFilms() {
        String sql =
                "SELECT F.FILM_ID, F.NAME,F.DESCRIPTION, F.RELEASE_DATE,  " +
                        "F.DURATION, F.MPA_ID, R.MPA_NAME " +
                        "FROM FILMS f " +
                        "JOIN MPA_RATINGS AS R ON f.MPA_ID = R.MPA_ID " +
                        "ORDER BY F.FILM_ID";
        log.info("Все фильмы:");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        if (getFilms().contains(film)) {
            log.error("Фильм: {} уже существует.", film.getName());
            throw new ValidationException("Фильм: " + film.getName() + " уже существует.");
        }
        if (!customValidator.isValid(film)) {
            log.info("Попытка добавить фильм с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }
        String sqlQuery = "INSERT INTO films (NAME,DESCRIPTION , RELEASE_DATE, DURATION, MPA_ID) " +
                            "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Добавлен фильм {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!(getFilms().contains(film))) {
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }
        String sql =
                "UPDATE FILMS " +
                        "SET NAME = ?,DESCRIPTION = ? ,RELEASE_DATE = ? , " +
                        "DURATION = ?, MPA_ID = ? " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        log.info("Обновлён фильм {}", film.getName());
        return film;
    }

    @Override
    public Film findById(int id) {
        if (id < 1) {
            throw new FilmNotFoundException("Введен некорректный идентификатор фильма.");
        }
        for (Film film: getFilms()) {
            if (film.getId() == id) {
                String sql =
                        "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE," +
                                "F.DURATION, F.MPA_ID, R.MPA_NAME " +
                                "FROM FILMS F " +
                                "JOIN MPA_RATINGS AS R ON F.MPA_ID = R.MPA_ID " +
                                "WHERE F.FILM_ID = ?";
                return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id)
                        .stream().findAny().orElse(null);
            }
        }
        log.error("Фильм не найден");
        throw new FilmNotFoundException("Фильм не найден");
    }

    @Override
    public void deleteFilm(Film film) {
        if (!getFilms().contains(film) || film.getId() < 1) {
            throw new FilmNotFoundException("Фильм не найден для удаления.");
        }
        String sql =
                "DELETE " +
                        "FROM FILMS " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
        log.info("Удалён фильм {}", film.getName());
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
        return film;
    }

    @Override
    public List<Film> popularFilms(Integer count) {
        String sql =
                "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION , F.RELEASE_DATE,  " +
                        "F.DURATION, F.MPA_ID, R.MPA_NAME " +
                        "FROM FILMS F " +
                        "JOIN MPA_RATINGS AS R ON f.MPA_ID = R.MPA_ID " +
                        "LEFT JOIN FILMS_LIKES L on F.FILM_ID = L.FILM_ID " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(L.USER_ID) DESC " +
                        "limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }
}