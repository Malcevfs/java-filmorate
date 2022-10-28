package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storrage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;
    ArrayList<Long> list3 = new ArrayList<>();

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(int id, int friendId) {
        if (inMemoryUserStorage.getUsersStorage().get(id) == null || inMemoryUserStorage.getUsersStorage().get(friendId) == null) {
            throw new StorageException("Не удалось добавить друга. Ошибка поиска пользователя");
        }

        inMemoryUserStorage.getUsersStorage().get(id).getFriends().add((long) friendId);
        inMemoryUserStorage.getUsersStorage().get(friendId).getFriends().add((long) id);
    }

    public void deleteFriend(int id, int friendId) {
        if (inMemoryUserStorage.getUsersStorage().get(id) == null || inMemoryUserStorage.getUsersStorage().get(friendId) == null) {
            throw new StorageException("Не удалось удалить друга. Ошибка поиска пользователя");
        }
        inMemoryUserStorage.getUsersStorage().get(id).getFriends().remove((long) friendId);
        inMemoryUserStorage.getUsersStorage().get(friendId).getFriends().remove((long) id);
    }

    public List<User> getAllFriends(int id) {
        if (inMemoryUserStorage.getUsersStorage().get(id) == null) {
            throw new StorageException("Не удалост показать друзей. Ошибка поиска пользователя");
        }
        List<User> friends = new ArrayList<>();

        for (Long friendsId : inMemoryUserStorage.getUsersStorage().get(id).getFriends()) {
            Integer newId = Math.toIntExact(friendsId);
            friends.add(inMemoryUserStorage.getUsersStorage().get(newId));
        }
        return friends;
    }

    public List<User> getOtherFriends(int id, int otherId) {
        List<User> friends = new ArrayList<>();

        ArrayList<Long> list1 = new ArrayList<>(inMemoryUserStorage.getUsersStorage().get(id).getFriends());
        ArrayList<Long> list2 = new ArrayList<>(inMemoryUserStorage.getUsersStorage().get(otherId).getFriends());

        for (int i = 1; i < list1.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    list3.add(list2.get(j));
                }
            }
        }
        for (Long friendsId : list3) {
            int newId = Math.toIntExact(friendsId);
            friends.add(inMemoryUserStorage.getUsersStorage().get((newId)));
        }
        return friends;
    }

}
