package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    protected int id = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> usersStorage = new HashMap<>();


    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        for (User users : usersStorage.values()) {
            if (users.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        createId();
        user.setId(id);

        usersStorage.put(user.getId(), user);
        log.info("Пользователь c id " + user.getId() + " добавлен в хранилище");
        return user;
    }

    @PutMapping
    public User refreshUserData(@Valid @RequestBody User user) {
        for (User users : usersStorage.values()) {
            if (users.getId() != user.getId()) {
                throw new ValidationException("Пользователя с таким id не существует в базе");
            }
        }

        usersStorage.put(user.getId(), user);
        log.info("Пользователь с id" + user.getId() + " изменены данные в хранилище");
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return usersStorage.values();
    }

    public int createId() {
        return id++;
    }
}

