package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storrage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserService userService;
    private final FilmService filmService;

    @Test
    @DisplayName("Проверка поиска пользователя с id=1")
    public void testFindUserById() {

        User user = userStorage.getUserById(1);

        Assertions.assertEquals(1, user.getId(), "Id пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("nikolas1990", user.getLogin(), "Login пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("1@ya.ru", user.getEmail(), "Email пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("Nicolas Cane", user.getName(), "Имя пользователя не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка поиска всех пользователей")
    public void getAllUsersTest() {
        List<User> users = userStorage.getAll();
        Assertions.assertNotNull(users, "Пользователей в выдаче нет");
        Assertions.assertEquals(3, users.size(), "Количество пользователей в выдаче не соответсвует ожидаемому");
        Assertions.assertEquals("Rufus Gonzales", users.get(1).getName(), "Количество пользователей в выдаче не соответсвует ожидаемому");
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void addUSerTest() {
        User user = userStorage.add(new User("test@mail.ru", "testLogin", "testUser", LocalDate.of(2000, 10, 10)));
        Assertions.assertEquals(4, user.getId(), "Id пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("testLogin", user.getLogin(), "Login пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("test@mail.ru", user.getEmail(), "Email пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("testUser", user.getName(), "Имя пользователя не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    public void refreshUserTest() {
        User user = new User("refresh@mail.ru", "newLogin", "testUserRefreshed", LocalDate.of(2001, 11, 11));
        user.setId(3);
        User refreshedUser = userStorage.refresh(user);
        Assertions.assertEquals(3, refreshedUser.getId(), "Id пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("newLogin", refreshedUser.getLogin(), "Login пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("refresh@mail.ru", refreshedUser.getEmail(), "Email пользователя не совпадает с ожидаемым");
        Assertions.assertEquals("testUserRefreshed", refreshedUser.getName(), "Имя пользователя не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка добавления в друзья")
    public void addAndGetFriendTest() {
        userService.addFriend(1, 2);
        userService.addFriend(1, 3);
        userService.addFriend(2, 3);

        List<User> friends = userService.getAllFriends(1);
        List<User> mutualFriend = userService.getOtherFriends(1, 2);

        Assertions.assertEquals(2, friends.get(0).getId(), "Id друга не совпадает с ожидаемым");
        Assertions.assertEquals(2, friends.size(), "Количество друзей у польщователей не совпадает с ожидаемым");
        Assertions.assertEquals(3, mutualFriend.get(0).getId(), "Общий друг польщователей не совпадает с ожидаемым");

    }

    @Test
    @DisplayName("Удаление пользователя из друзей")
    public void deleteFriendTest() {
        userService.deleteFriend(1, 2);
        List<User> friends = userService.getAllFriends(1);

        Assertions.assertEquals(1, friends.size(), "Количество друзей у польщователей не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка на взаимность дружбы")
    public void mutualFriendship() {
        Assertions.assertFalse(userStorage.checkFriendRequest(1, 3), "Ошибка взаимности дружбы. Ожидалось - false");
        userService.addFriend(3, 1);
        Assertions.assertFalse(userStorage.checkFriendRequest(1, 3), "Ошибка взаимности дружбы. Ожидалось - true");
    }

    @Test
    @DisplayName("Проверка поиска фильма с id=1")
    public void testFindFilmById() {

        Film film = filmDbStorage.getFilmById(1);

        Assertions.assertEquals(1, film.getId(), "Id фильма не совпадает с ожидаемым");
        Assertions.assertEquals("Dune", film.getName(), "Название фильма не совпадает с ожидаемым");
        Assertions.assertEquals("dune description", film.getDescription(), "Описание фильма не совпадает с ожидаемым");
        Assertions.assertEquals(150, film.getDuration(), "Продолжительность фильма не совпадает с ожидаемым");
        Assertions.assertEquals("G", film.getMpa().getName(), "MPA фильма не соответсвует ожидаемому");

        for (Genre genres : film.getGenres()) {
            Assertions.assertEquals("Комедия", genres.getName(), "Жанр фильма не совпадает с ожидаемым");
        }
    }

    @Test
    @DisplayName("Проверка поиска всех фильмов")
    public void getAllFilmsTest() {
        List<Film> films = filmDbStorage.getAll();
        Assertions.assertNotNull(films, "Фильмов в выче нет");
        Assertions.assertEquals(4, films.size(), "Количество фильмов в выдаче не соответсвует ожидаемому");
        Assertions.assertEquals("Tomb Rider", films.get(1).getName(), "Количество фильмов в выдаче не соответсвует ожидаемому");
    }

    @Test
    @DisplayName("Проверка создания фильма")
    public void addFilmTest() {
        Film film = new Film("filmName", "filmDescription", LocalDate.of(2000, 10, 10), 100);
        film.setMpa(new Mpa(1, "G"));
        filmDbStorage.add(film);

        Assertions.assertEquals(4, film.getId(), "Id фильма не совпадает с ожидаемым");
        Assertions.assertEquals("filmName", film.getName(), "Название фильма не совпадает с ожидаемым");
        Assertions.assertEquals("filmDescription", film.getDescription(), "Описание фильма не совпадает с ожидаемым");
        Assertions.assertEquals(100, film.getDuration(), "Продолжительность фильма не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка обновления фильма")
    public void refreshFilmTest() {
        Film film = new Film("refreshFilmName", "newDescription", LocalDate.of(2000, 10, 10), 120);
        film.setId(3);
        film.setMpa(new Mpa(2, "PG"));

        Film refreshedFilm = filmDbStorage.refresh(film);
        Assertions.assertEquals(3, refreshedFilm.getId(), "Id фильма не совпадает с ожидаемым");
        Assertions.assertEquals("refreshFilmName", refreshedFilm.getName(), "Название фильма не совпадает с ожидаемым");
        Assertions.assertEquals("newDescription", refreshedFilm.getDescription(), "Описание фильма не совпадает с ожидаемым");
        Assertions.assertEquals(120, refreshedFilm.getDuration(), "Продолжительность фильма не совпадает с ожидаемым");
        Assertions.assertEquals("PG", refreshedFilm.getMpa().getName(), "Название MPA не совпавдвет с ожидаемым");
    }

    @Test
    @DisplayName("Проверка добавления лайков")
    public void addLikeTest() {
        filmService.addLike(1, 1);
        Set<Long> filmLike = filmDbStorage.getFilmById(1).getLikes();
        Long like = 0L;
        for (Long likes : filmLike) {
            like = likes;
        }
        Assertions.assertEquals(1, like, "Лайк для фильма не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка удаления лайка")
    public void deleteLikeTest() {
        filmService.deleteLike(1, 1);
        Set<Long> filmLike = filmDbStorage.getFilmById(1).getLikes();
        Assertions.assertEquals(0, filmLike.size(), "Количество лайков для фильма не совпадает с ожидаемым");
    }
}