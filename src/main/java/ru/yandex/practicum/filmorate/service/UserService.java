package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storrage.user.InMemoryUserStorage;

import java.util.Collection;

@Service
public class UserService {

    public void addFriend(User user) {
        user.getFriends().add((long) user.getId());
    }

    public void deleteFriend(User user) {
        user.getFriends().remove(user.getId());
    }

    public User getAllFriends(User user) {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        for (User users : inMemoryUserStorage.getUsersStorage().values()) {
            if (user.getFriends().contains(users.getId())) {
                return users;
            }
        }
        return null;
    }
}
