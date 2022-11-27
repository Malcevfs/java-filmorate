package ru.yandex.practicum.filmorate.storrage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    protected int id = 0;
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Integer, User> usersStorage = new HashMap<>();

    public Map<Integer, User> getUsersStorage() {
        return usersStorage;
    }

    public User add(User user) {
        for (User users : usersStorage.values()) {
            if (users.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        id++;
        user.setId(id);

        usersStorage.put(user.getId(), user);
        log.info("Пользователь c id " + user.getId() + " добавлен в хранилище");
        return user;
    }

    public User refresh(User user) {
        for (User users : usersStorage.values()) {
            if (users.getId() != user.getId()) {
                throw new StorageException("Пользователя с таким id не существует в базе");
            }
        }

        usersStorage.put(user.getId(), user);
        log.info("Пользователь с id" + user.getId() + " изменены данные в хранилище");
        return user;
    }

    public Collection<User> getAll() {
        if (usersStorage.isEmpty()) {
            throw new StorageException("Нет пользователей в хранилище");
        }
        return usersStorage.values();
    }

    @Override
    public Collection<User> getUserById(int id) {
        if (usersStorage.get(id) == null) {
            throw new StorageException("Пользователя с таким Id нет в хранилище");
        }
        return (Collection<User>) usersStorage.get(id);
    }

}
