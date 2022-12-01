package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public void addFriend(int id, int friendId) {
        userDbStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userDbStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(int id) {
        return userDbStorage.getAllFriends(id);
    }

    public List<User> getOtherFriends(int id, int otherId) {
        return userDbStorage.getOtherFriends(id, otherId);
    }

}

