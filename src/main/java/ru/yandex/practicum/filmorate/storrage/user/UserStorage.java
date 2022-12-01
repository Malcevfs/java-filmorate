package ru.yandex.practicum.filmorate.storrage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User add(User user);

    User refresh(User user);

    Collection<User> getAll();

    User getUserById(int id);
}
