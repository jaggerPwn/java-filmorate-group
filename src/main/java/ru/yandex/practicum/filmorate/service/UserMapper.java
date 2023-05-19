package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO userToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    public static User userToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .login(userDTO.getLogin())
                .name(userDTO.getName())
                .birthday(userDTO.getBirthday())
                .build();
    }

    public static List<UserDTO> listUsersToListUserDto(Collection<User> users) {
        return users.stream().map(UserMapper::userToUserDTO).collect(Collectors.toList());
    }

}
