package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDBStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director saveDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("directors")
                .usingGeneratedKeyColumns("directorid");
        Number key = simpleJdbcInsert.executeAndReturnKey(director.directorToMap());
        director.setId((Long) key);
        log.debug("Director создан с ID {}.", director.getId());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        if (director == null || director.getId() == null) {
            throw new ValidationException("Невалидный Director");
        }
        String sqlQuery = "UPDATE directors SET name = ? WHERE directorid = ?";
        if (jdbcTemplate.update(sqlQuery, director.getName(), director.getId()) != 0) {
            log.debug("Director {} успешно обновлён", director.getId());
            return director;
        } else {
            throw new EntityNotFoundException("Director с таким id не найден");
        }
    }

    @Override
    public List<Director> readAllDirectors() {
        String sqlQuery = "SELECT * FROM directors";
        log.debug("Все Director получены");
        return jdbcTemplate.query(sqlQuery, this::mapToDirector);
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        String sqlQuery = "SELECT * FROM directors WHERE directorid = ?";
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRow.next()) {
            Director director = Director.builder()
                    .id(directorRow.getLong("directorid"))
                    .name(directorRow.getString("name"))
                    .build();
            log.debug("Получен Director с ID {}, по имени {} ", directorRow.getLong("directorid"),
                    directorRow.getString("name"));
            return Optional.of(director);
        } else {
            log.debug("Director с ID: {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public Director deleteDirectorById(Long id) {
        String sqlQuery = "DELETE FROM directors WHERE directorid = ?";
        Optional<Director> director = getDirectorById(id);
        jdbcTemplate.update(sqlQuery, id);
        log.debug("Director с ID {} удален", id);
        return director.orElse(null);
    }

    public Director mapToDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("directorid"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public Set<Director> getDirectorsByFilmId(Long filmId) {
        String sqlQuery = "SELECT fd.directorid, name FROM filmdirectors AS fd INNER JOIN directors AS d " +
                "ON d.directorid = fd.directorid WHERE filmid = ?";
        List<Director> directors = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new Director(rs.getLong("directorid"), rs.getString("name")), filmId);
        log.debug("Получен список Directors для Film с id {}", filmId);
        return new HashSet<>(directors);
    }
}
