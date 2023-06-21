package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Slf4j
@Component("filmDBStorage")
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDBStorage mpaDBStorage;
    private final GenreStorage genreStorage;
    private final LikeDBStorage likeDBStorage;
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmDBStorage(JdbcTemplate jdbcTemplate, MpaDBStorage mpaDBStorage, GenreStorage genreStorage, LikeDBStorage likeDBStorage, DirectorStorage directorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDBStorage = mpaDBStorage;
        this.genreStorage = genreStorage;
        this.likeDBStorage = likeDBStorage;
        this.directorStorage = directorStorage;
    }

    @Override
    public Film saveFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(film.filmToMap());
        film.setId((Long) key);
        film.setMpa(mpaDBStorage.readById(film.getMpa().getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String query = "INSERT INTO filmgenres (filmid,genreid) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }
        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));

        updateDirector(film);
        log.debug("Film c ID {} сохранён.", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Film не найден");
        }
        String sqlQuery
                = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpaid = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()) != 0) {
            log.info("Film c id {} обновлён", film.getId());
        } else {
            throw new EntityNotFoundException("Film с таким id не существует");
        }

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            String sqlDelete = "DELETE FROM filmgenres WHERE filmid = ?";
            jdbcTemplate.update(sqlDelete, film.getId());

        } else {
            String sqlDelete2 = "DELETE FROM filmgenres WHERE filmid = ?";
            jdbcTemplate.update(sqlDelete2, film.getId());
            String sqlUpdate = "INSERT INTO filmgenres (filmid, genreid) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlUpdate, film.getId(), genre.getId());
            }
        }
        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));
        String sqlQueryDeleteDirector = "DELETE FROM filmdirectors WHERE filmid = ?";
        jdbcTemplate.update(sqlQueryDeleteDirector, film.getId());
        updateDirector(film);
        return film;
    }

    @Override
    public List<Film> readAllFilms() {
        String query = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(query, this::mapToFilm);
        log.debug("Получены все Film.");
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        String query = "SELECT * FROM films WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, id);
        if (sqlRowSet.first()) {
            Film film = Film.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .description(sqlRowSet.getString("description"))
                    .releaseDate(sqlRowSet.getDate("releaseDate").toLocalDate())
                    .duration(sqlRowSet.getInt("duration"))
                    .genres(genreStorage.getGenresByFilmID(sqlRowSet.getLong("id")))
                    .likes(likeDBStorage.getLikerByFilmId(sqlRowSet.getLong("id")))
                    .directors(directorStorage.getDirectorsByFilmId(sqlRowSet.getLong("id")))
                    .mpa(mpaDBStorage.readById(sqlRowSet.getInt("mpaid")))
                    .build();
            log.debug("Получен Film с ID {}.", id);
            return film;
        } else {
            throw new EntityNotFoundException("Film не найден");
        }
    }

    @Override
    public Set<Film> getTopFilms(Long count) {
        String query = "SELECT id, name, description, releaseDate, duration, mpaid FROM films " +
                "LEFT JOIN likes ON id = filmid GROUP BY id ORDER BY count(userid) desc  LIMIT ?";
        List<Film> topFilms = jdbcTemplate.query(query, this::mapToFilm, count);
        log.debug("Получаем топ {} Film по кол-ву Likes.", count);
        return new HashSet<>(topFilms);
    }

    @Override
    public List<Film> getSortedFilms(Long id, String sortBy) {
        String sqlQueryLikes = "SELECT f.* "
                + "FROM films as f "
                + "LEFT JOIN filmdirectors AS fd ON f.id = fd.filmid "
                + "LEFT JOIN likes AS l ON f.id = l.filmid "
                + "WHERE fd.directorid = ?"
                + "GROUP BY f.id "
                + "ORDER BY COUNT(l.userid) DESC";
        String sqlQueryYears = "SELECT * "
                + "FROM films AS f "
                + "LEFT JOIN filmdirectors AS fd ON f.id = fd.filmid "
                + "WHERE fd.directorid = ?"
                + "ORDER BY releaseDate";
        List<Film> films;
        if (sortBy.equals("likes")) {
            films = jdbcTemplate.query(sqlQueryLikes, this::mapToFilm, id);
        } else if (sortBy.equals("year")) {
            films = jdbcTemplate.query(sqlQueryYears, this::mapToFilm, id);
        } else {
            throw new EntityNotFoundException("Некорректный запрос");
        }
        return films;
    }

    private void updateDirector(Film film) {
        if (film.getDirectors() != null) {
            jdbcTemplate.batchUpdate("INSERT INTO filmdirectors (filmid, directorid) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, film.getId());
                            ps.setLong(2, new ArrayList<>(film.getDirectors()).get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return film.getDirectors().size();
                        }
                    });
        }
    }

    public Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaDBStorage.readById(rs.getInt("mpaid")))
                .likes(likeDBStorage.getLikerByFilmId(rs.getLong("id")))
                .genres(genreStorage.getGenresByFilmID(rs.getLong("id")))
                .directors(directorStorage.getDirectorsByFilmId(rs.getLong("id")))
                .build();
    }

    @Override
    public void deleteFilm(Long id) {
        String query = "DELETE FROM films WHERE id = ?";
        if (jdbcTemplate.update(query, id) != 0) {
            log.info("Film с Id {} удалён.", id);
        } else {
            log.info("Film с Id {} не найден.", id);
        }
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) { //TODO прогнать тесты в Postman

        String query = "SELECT * FROM films f "
                + "LEFT JOIN likes l1 ON f.id = l1.filmid "
                + "LEFT JOIN likes l2 ON f.id = l2.filmid "
                + "WHERE l1.userId =? AND l2.userId =? "
                + "GROUP BY f.id "
                + "ORDER BY count (l1.userId) DESC";

        List<Film> commonFilms = jdbcTemplate.query(query, this::mapToFilm, userId, friendId);
        log.debug("Получены фильмы общие у Users userId {} и friend id {}.", userId, friendId);
        return commonFilms;
    }

}
