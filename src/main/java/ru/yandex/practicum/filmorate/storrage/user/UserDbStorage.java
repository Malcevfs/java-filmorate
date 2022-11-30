package ru.yandex.practicum.filmorate.storrage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    protected int id = 0;
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        String sqlQuery = "select * from USERS";

        List<User> user1 = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
        for (User users : user1) {
            if (users.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        String sql = "INSERT INTO users (email,login,name,birthday) VALUES ('" + user.getEmail() + "'" + "," + "'" +
                user.getLogin() + "'" + "," + "'" + user.getName() + "'" + "," + "'" + user.getBirthday() + "'" + ")";
        jdbcTemplate.update(sql);

        String sql1 = "select * from users where login='" + user.getLogin() + "'" +
                " and email ='" + user.getEmail() + "'";
        List<User> user2 = jdbcTemplate.query(sql1, (rs, rowNum) -> makeUser(rs));

        user.setId(user2.get(0).getId());


        log.info("Пользователь с id " + user.getId() + ". Д обавлен в хранилище");
        return user;
    }

    @Override
    public User refresh(User user) {

        if (checkUserId(user.getId())) {
            throw new StorageException("Пользователя с таким id не существует в базе");
        }

        String sql = "update users set " +
                "email ='" + user.getEmail() + "'" + "," +
                "login ='" + user.getLogin() + "'" + "," +
                "name ='" + user.getName() + "'" + "," +
                "birthday ='" + user.getBirthday() + "'" +
                "where user_id=" + user.getId();
        jdbcTemplate.update(sql);

        log.info("Пользователь с id " + user.getId() + ". Изменены данные в хранилище");

        return user;
    }


    @Override
    public List<User> getAll() {

        List<User> user1 = new ArrayList<>();
        String sqlQuery = " select * from USERS";

        /* Не получалось получить корректные значения лайков для всех юзеров. Путаются значения в листах.
            Решил пока сделать  через метод получения юзера по id **/

        List<User> user = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
        if (user.isEmpty()) {
            throw new StorageException("Нет пользователей в хранилище");
        }
        for (int i = 1; i <= user.size(); i++) {
            user1.add(getUserById(i));
        }
        return user1;
    }

    @Override
    public User getUserById(int id) {
        if (checkUserId(id)) {
            throw new StorageException("Пользователя с таким Id нет в хранилище");
        }

        String sql = "select * from USERS " +
                "where USER_ID =" + id;
        List<User> user = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

        String sql1 = "select * from FRIENDS_REQUEST " +
                "WHERE USER_ID =" + id;
        List<Integer> friendId = jdbcTemplate.query(sql1, (rs, rowNum) -> getFriends(rs));

        for (Integer friend : friendId) {
            user.get(0).getFriends().add(Long.valueOf(friend));
        }

        return user.get(0);
    }

    private User makeUser(ResultSet rs) throws SQLException {

        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(email, login, name, birthday);
        user.setId(id);

        return user;
    }

    private int getFriends(ResultSet rs) throws SQLException {

        return rs.getInt("friend_id");
    }

    public boolean checkUserId(int id) {
        boolean check = false;
        String sql = String.format("select * from USERS where USER_ID =%d", id);
        List<User> user = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        if (!user.isEmpty()) {
            check = true;
        }
        return !check;
    }

    public void addFriend(int id, int friendId) {
        if (getUserById(id) == null || getUserById(friendId) == null) {
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


    public void deleteFriend(int id, int friendId) {
        if (getUserById(id) == null || getUserById(friendId) == null) {
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
        if (getUserById(id) == null) {
            throw new StorageException("Не удалось показать список друзей. Ошибка поиска пользователя");
        }
        List<User> friends = new ArrayList<>();

        for (Long friendsId : getUserById(id).getFriends()) {
            int newId = Math.toIntExact(friendsId);
            friends.add(getUserById(newId));
        }
        return friends;
    }

    public List<User> getOtherFriends(int id, int otherId) {

        if (id < 0 || otherId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }
        List<User> friends = new ArrayList<>();
        ArrayList<Long> list3 = new ArrayList<>();

        if (checkUserId(id) || checkUserId(otherId)) {
            return friends;
        }

        ArrayList<Long> list1 = new ArrayList<>(getUserById(id).getFriends());
        ArrayList<Long> list2 = new ArrayList<>(getUserById(otherId).getFriends());

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
            friends.add(getUserById(newId));
        }
        return friends;
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
}
