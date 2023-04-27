package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserServiceImpl implements UserService {

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
            throw new RuntimeException("Нет такого ID");
        }
    }

    @Override
    public List<User> readAllUsers() {
        log.debug("Вывод списка фильмов");
        return new ArrayList<>(users.values());
    }

}
