package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Integer id) {
        if (id < 1) {
            throw new EntityNotFoundException("Некорректный идентификатор возрастного ограничения");
        }
        String sql =
                "SELECT * " +
                        "FROM MPA_RATINGS " +
                        "WHERE MPA_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql =
                "SELECT * " +
                        "FROM MPA_RATINGS " +
                        "ORDER BY MPA_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
    }
}