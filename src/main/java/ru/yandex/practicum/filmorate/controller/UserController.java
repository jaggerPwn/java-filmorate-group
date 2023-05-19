package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.UserDTO;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO newUser) {
        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO newUser) {

        return new ResponseEntity<>(userService.updateUser(newUser), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> readAllUsers() {
        return new ResponseEntity<>(userService.readAllUsers(), HttpStatus.OK);
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFiend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendById(id, friendId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<UserDTO>> readAllFriendsByUserId(@PathVariable Long id) {
        return new ResponseEntity<>(userService.readAllFriendsByUserId(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDTO>> readAllCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return new ResponseEntity<>(userService.readAllCommonFriends(id, otherId), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

}
