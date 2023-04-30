package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService = new UserServiceImpl();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        try {
            Validator.userValidator(newUser);
            return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.CREATED);
        } catch (ValidationException e) {
            return new ResponseEntity<>(newUser, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User newUser) {
        try {
            Validator.userValidator(newUser);
            return new ResponseEntity<>(userService.updateUser(newUser), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<>(newUser, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> readAllUsers() {
        return new ResponseEntity<>(userService.readAllUsers(), HttpStatus.OK);
    }

}
