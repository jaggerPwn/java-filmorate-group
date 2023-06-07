package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("userDBStorage")
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User saveUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(user.userToMap());
        user.setId((Long) key);
        log.debug("Юзер создан с ID {}.", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) { // TODO нужно ли обновлять друзей ?
        if (user == null || user.getId() == null) {
            throw new ValidationException("Невалидный юзер");
        }
        if (getUserById(user.getId()) == null) {
            throw new EntityNotFoundException("Юзер с таким id не найден");
        }
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name =?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.debug(" User {} успешно обновлён", user.getId());
        return user;
    }

    @Override
    public List<User> readAllUsers() {
        String sqlQuery = "SELECT * FROM users ";
        log.debug("Все юзеры выведены");
        return jdbcTemplate.query(sqlQuery, this::mapToUser);
    }

    @Override
    public User getUserById(Long id) {
//        String sqlQuery = String.format("SELECT * FROM users WHERE id = %d", id);
//        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery);
//        User user = null;
//        if (userRows.next()) {
//            user = User.builder()
//                    .id(userRows.getLong("id"))
//                    .email(userRows.getString("email"))
//                    .login(userRows.getString("login"))
//                    .name(userRows.getString("name"))
//                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
//                    .friends(findFriendsByUserId(userRows.getLong("id")))
//                    .build();
//        } else {
//            throw new EntityNotFoundException("Юзер с таким id не найден");
//        }
//        return user;
        String sqlQuery = String.format("SELECT * FROM users WHERE id = %d", id);
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, this::mapToUser);
            log.debug("Юзер с ID {} получен.", id);
            return user;
        } catch (Throwable throwable) {
            throw new EntityNotFoundException("Юзер с таким id не найден");
        }
    }

    private Set<Long> findFriendsByUserId(Long id) {
        String sqlQuery = "SELECT friendid FROM friends WHERE userid = ?";
        Set<Long> ids = new HashSet<>();
        SqlRowSet friends = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (friends.next()) {
            ids.add(friends.getLong("friendid"));
        }
        return ids;
    }

    public User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate()) //TODO добавить друзей
                .friends(findFriendsByUserId(rs.getLong("id")))
                .build();
    }

    @Override
    public void userAddFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId); // если юзер отсутствует - будет выброшено исключение
        String query = "INSERT INTO friends (userid, friendid) VALUES (?,?)";
        try {
            jdbcTemplate.update(query, userId, friendId);
            log.debug("Friend с ID {} добавлен успешкно", friendId);
        } catch (DuplicateKeyException e) {
            log.debug("Ошибка добавления.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public void userDeleteFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId); // для валидации
        String query = "DELETE FROM friends WHERE userid = ? AND friendid = ?";
        if (jdbcTemplate.update(query, userId, friendId) != 0) {
            log.debug("У Юзера с ID {} удалён Друг {}", userId, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого юзера");
        }

    }

    @Override
    public List<User> getAllFriendByUserId(Long id) {
        User user = getUserById(id);
        String query = "SELECT friendid FROM friends WHERE userid = ?";
        List<Long> friends = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("friendid"), id);
        List<User> friends2 = new ArrayList<>();
        for (Long friend : friends) {
            friends2.add(getUserById(friend));
        }
        log.debug("Получен список friendId друзей у User с id {}.", id);
        return friends2;
    }

}
