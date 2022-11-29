package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addFriend(int id, int friendId) {
        if (userDbStorage.getUserById(id) == null || userDbStorage.getUserById(friendId) == null) {
            throw new StorageException("Не удалось поставить лайк. Ошибка поиска пользователя");
        }
        if (id < 0 || friendId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }
        String sql = String.format("INSERT INTO friends_request(user_id, friend_id, request) " +
                "VALUES ('%d','%d', false)", id, friendId);
        jdbcTemplate.update(sql);

        boolean request = checkFriendRequest(id, friendId);

        if (request) {
            String sql1 = String.format("update friends_request " +
                    "set user_id =%d,friend_id =%d,request =%s", id, friendId, true);

            jdbcTemplate.update(sql1);
            String sql2 = String.format("update friends_request " +
                    "set user_id =%d,friend_id =%d,request =%s", friendId, id, true);

            jdbcTemplate.update(sql2);
        }
    }

    public boolean checkFriendRequest(int userId, int friendId) {

        if (userId < 0 || friendId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }

        boolean request = false;
        String getUserRequest = String.format(" select REQUEST from FRIENDS_REQUEST " +
                "where USER_ID =%d and FRIEND_ID =%d", userId, friendId);
        List<Boolean> userRequest = jdbcTemplate.query(getUserRequest, (rs, rowNum) -> getFriendRequest(rs));

        String getFriendRequest = String.format(" select REQUEST from FRIENDS_REQUEST " +
                "where USER_ID =%d and FRIEND_ID =%d", friendId, userId);
        List<Boolean> friendRequest = jdbcTemplate.query(getFriendRequest, (rs, rowNum) -> getFriendRequest(rs));
        if (friendRequest.isEmpty()) {
           return false;

        } else if (userRequest.get(0) == friendRequest.get(0)) {
            request = true;
        }
        return request;
    }

    private Boolean getFriendRequest(ResultSet rs) throws SQLException {

        return rs.getBoolean("request");
    }

    public void deleteFriend(int id, int friendId) {
        if (userDbStorage.getUserById(id) == null || userDbStorage.getUserById(friendId) == null) {
            throw new StorageException("Не удалось удалить друга. Ошибка поиска пользователя");
        }
        if (id < 0 || friendId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }
        String sql = String.format("DELETE FROM FRIENDS_REQUEST " +
                "WHERE USER_ID =%dAND FRIEND_ID =%d", id, friendId);
        jdbcTemplate.update(sql);
    }

    public List<User> getAllFriends(int id) {
        if (userDbStorage.getUserById(id) == null) {
            throw new StorageException("Не удалось показать список друзей. Ошибка поиска пользователя");
        }
        List<User> friends = new ArrayList<>();

        for (Long friendsId : userDbStorage.getUserById(id).getFriends()) {
            int newId = Math.toIntExact(friendsId);
            friends.add(userDbStorage.getUserById(newId));
        }
        return friends;
    }

    public List<User> getOtherFriends(int id, int otherId) {

        if (id < 0 || otherId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }
        List<User> friends = new ArrayList<>();
        ArrayList<Long> list3 = new ArrayList<>();

        if (userDbStorage.checkUserId(id) || userDbStorage.checkUserId(otherId)) {
            return friends;
        }

        ArrayList<Long> list1 = new ArrayList<>(userDbStorage.getUserById(id).getFriends());
        ArrayList<Long> list2 = new ArrayList<>(userDbStorage.getUserById(otherId).getFriends());

        if (list2.isEmpty()) {
            return friends;
        }
        for (Long aLong : list1) {
            for (Long value : list2) {
                if (aLong.equals(value)) {
                    list3.add(value);
                }
            }
        }
        for (Long friendsId : list3) {
            int newId = Math.toIntExact(friendsId);
            friends.add(userDbStorage.getUserById(newId));
        }
        return friends;
    }
}

