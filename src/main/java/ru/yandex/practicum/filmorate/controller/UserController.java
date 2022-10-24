package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storrage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
private final UserStorage userStorage;
    @Autowired
    public UserController(UserStorage userStorage){
        this.userStorage = userStorage;
    }
    @PostMapping
    public User add(@Valid @RequestBody User user) {

        return userStorage.add(user);
    }

    @PutMapping
    public User refresh(@Valid @RequestBody User user) {

        return userStorage.refresh(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

}

