package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storrage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;
    private final UserDbStorage userDbStorage;

    @PostMapping
    public User add(@Valid @RequestBody User user) {

        return userDbStorage.add(user);
    }

    @PutMapping
    public User refresh(@Valid @RequestBody User user) {

        return userStorage.refresh(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userDbStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userDbStorage.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable("id") Integer id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getOtherFriendsList(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userService.getOtherFriends(id, otherId);
    }
}


