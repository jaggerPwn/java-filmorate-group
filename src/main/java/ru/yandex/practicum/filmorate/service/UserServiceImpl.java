package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserDTO;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage us;

    @Autowired
    public UserServiceImpl(UserStorage us) {
        this.us = us;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = UserMapper.userToUser(userDTO);
        if (Validator.userValidator(user)) {
            log.debug("Юзер " + userDTO + " сохранён.");
            return UserMapper.userToUserDTO(us.saveUser(user));
        }
        throw new ValidationException("Валидация юзера" + userDTO + " не пройдена");
    }

    @Override
    public UserDTO updateUser(UserDTO userDto) {
        User user = UserMapper.userToUser(userDto);
        if (Validator.userValidator(user)) {
            log.debug("У юзера " + userDto + " обновлён статус");
            return UserMapper.userToUserDTO(us.updateUser(user));
        }
        throw new ValidationException("Валидация юзера" + userDto + " не пройдена");
    }

    @Override
    public List<UserDTO> readAllUsers() {
        log.debug("Вывод списка фильмов");
        return UserMapper.listUsersToListUserDto(us.readAllUsers());
    }

    @Override
    public void addFiend(Long userId, Long friendId) {
        User user = us.getUserById(userId);
        User friend = us.getUserById(friendId);
        user.getFriends().add(friendId); // нужно ли проверять что они уже друзья ?
        friend.getFriends().add(userId);
        log.debug("Юзеру " + userId + " добавлен друг " + friendId);
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.debug("Юзер " + id + " возвращён");
        return UserMapper.userToUserDTO(us.getUserById(id));
    }

    @Override
    public void deleteFriendById(Long idUser, Long idFriend) {
        User user = us.getUserById(idUser);
        User friend = us.getUserById(idFriend);
        user.getFriends().remove(idFriend);
        friend.getFriends().remove(idUser);
        log.debug("Дружба между " + idUser + " и " + idFriend + " аннулирована.");
    }

    @Override
    public List<UserDTO> readAllFriendsByUserId(Long idUser) {
        User user = us.getUserById(idUser);
        Set<Long> ids = user.getFriends();
        List<UserDTO> friends = new ArrayList<>();
        for (Long id : ids) {
            friends.add(UserMapper.userToUserDTO(us.getUserById(id)));
        }
        log.debug("Список друзей у Юзера " + idUser + " был возвращён");
        return friends;
    }

    @Override
    public List<UserDTO> readAllCommonFriends(Long idUser1, Long idUser2) {
        Set<Long> ids = new HashSet<>(us.getUserById(idUser1).getFriends());
        ids.retainAll(us.getUserById(idUser2).getFriends());
        log.debug("Возвращён список общих друзей у " + idUser1 + " и " + idUser2);
        return ids.stream().map(us::getUserById).map(UserMapper::userToUserDTO).collect(Collectors.toList());
    }
}
