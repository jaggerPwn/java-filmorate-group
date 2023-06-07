package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {


    private final Map<Long, User> users = new HashMap<>(); // для хранения пользователей
    private Long idUser = 1L;

    @Override
    public User saveUser(User user) {
        user.setId(idUser);
        users.put(user.getId(), user);
        idUser++;
        log.debug("Пользователь создан");
        return user;
    }

    @Override
    public User updateUser(User user) {

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Пользователь успешно обновлён");
            return user;
        } else {
            log.debug("Пользователь не обновлён");
            throw new EntityNotFoundException("Нет такого ID");
        }
    }

    @Override
    public List<User> readAllUsers() {
        log.debug("Вывод списка фильмов");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            log.debug("Юзер получен по ID");
            return users.get(id);
        } else {
            log.debug("Юзер не получен по ID");
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void userAddFriend(Long userId, Long friendId) {

    }

    @Override
    public void userDeleteFriend(Long userId, Long friendId) {

    }

    @Override
    public List<User> getAllFriendByUserId(Long id) {
        return null;
    }
}
