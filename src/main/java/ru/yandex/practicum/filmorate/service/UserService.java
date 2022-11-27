package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storrage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;
    private final JdbcTemplate jdbcTemplate;

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage, JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addFriend(int id, int friendId) {
//        if (inMemoryUserStorage.getUsersStorage().get(id) == null || inMemoryUserStorage.getUsersStorage().get(friendId) == null) {
//            throw new StorageException("Не удалось добавить друга. Ошибка поиска пользователя");
//        }

        String sql = "INSERT INTO friends_request(user_id, friend_id, request) VALUES ('" + id + "'" + "," + "'" + friendId + "'" + ", false)";
        jdbcTemplate.update(sql);

        boolean request = checkFriendRequest(id, friendId);

        if (request) {
            String sql1 = "update friends_request set " +
                    "user_id =" + id + "," +
                    "friend_id =" + friendId + "," +
                    "request =" + true;

            jdbcTemplate.update(sql1);
            String sql2 = "update friends_request set " +
                    "user_id =" + friendId + "," +
                    "friend_id =" + id + "," +
                    "request =" + true;

            jdbcTemplate.update(sql2);
        }
    }

    public boolean checkFriendRequest(int userId, int friendId) {
        boolean request = false;
        String getUserRequest = " select REQUEST from FRIENDS_REQUEST where USER_ID =" + userId + " and FRIEND_ID =" + friendId;
        List<Boolean> userRequest = jdbcTemplate.query(getUserRequest, (rs, rowNum) -> getFriendRequest(rs));

        String getFriendRequest = " select REQUEST from FRIENDS_REQUEST where USER_ID =" + friendId + " and FRIEND_ID =" + userId;
        List<Boolean> friendRequest = jdbcTemplate.query(getFriendRequest, (rs, rowNum) -> getFriendRequest(rs));
        if (friendRequest.isEmpty()) {
            request = false;
        } else if (userRequest.get(0) == friendRequest.get(0)) {
            request = true;
        }
        return request;
    }

    private Boolean getFriendRequest(ResultSet rs) throws SQLException {
        Boolean request = rs.getBoolean("request");

        return request;
    }

    public void deleteFriend(int id, int friendId) {
//        if (inMemoryUserStorage.getUsersStorage().get(id) == null || inMemoryUserStorage.getUsersStorage().get(friendId) == null) {
//            throw new StorageException("Не удалось удалить друга. Ошибка поиска пользователя");
//        }
        String sql = "DELETE FROM FRIENDS_REQUEST " +
                "WHERE USER_ID =" + id +
                "AND FRIEND_ID =" + friendId;
        jdbcTemplate.update(sql);
    }

    public List<User> getAllFriends(int id) {
//        if (inMemoryUserStorage.getUsersStorage().get(id) == null) {
//            throw new StorageException("Не удалост показать друзей. Ошибка поиска пользователя");
//        }
        List<User> friends = new ArrayList<>();

        for (Long friendsId : userDbStorage.getUserById(id).get(0).getFriends()) {
            Integer newId = Math.toIntExact(friendsId);
            friends.add(userDbStorage.getUserById(newId).get(0));
        }
        return friends;
    }

    public List<User> getOtherFriends(int id, int otherId) {
        List<User> friends = new ArrayList<>();
        ArrayList<Long> list1 = new ArrayList<>();
        ArrayList<Long> list2 = new ArrayList<>();
        ArrayList<Long> list3 = new ArrayList<>();

        for (User user : userDbStorage.getUserById(id)) {
            list1.addAll(user.getFriends());
        }
        for (User user : userDbStorage.getUserById(otherId)) {
            list2.addAll(user.getFriends());
        }

        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    list3.add(list2.get(j));
                }
            }
        }
        for (Long friendsId : list3) {
            int newId = Math.toIntExact(friendsId);
            friends.addAll(userDbStorage.getUserById(newId));
        }
        return friends;
    }
}

