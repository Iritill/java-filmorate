package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getInMemoryUserStorage().allUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Long id) {
        return userService.allFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.getCommonFriends(id,otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.getInMemoryUserStorage().addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return userService.getInMemoryUserStorage().updateUser(newUser);

    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") Long id) {
        return userService.getInMemoryUserStorage().getUserById(id);
    }
}
