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
}
