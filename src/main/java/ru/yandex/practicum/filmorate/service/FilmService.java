package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storrage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storrage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storrage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FilmService {
    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

    FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage, FilmDbStorage filmDbStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmDbStorage = filmDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(int id, int userId) {
//        if (filmStorage.getFilmsStorage().get(id) == null || userStorage.getUsersStorage().get(userId) == null) {
//            throw new StorageException("Не удалось поставить лайк. Ошибка поиска пользователя");
//        }
//        filmStorage.getFilmsStorage().get(id).getLikes().add((long) userId);
        String sql = "INSERT INTO likes(film_id, user_id) VALUES ('" + id + "'" + "," + "'" + userId + "')";
        jdbcTemplate.update(sql);
    }

    public void deleteLike(int id, int userId) {
//        if (filmStorage.getFilmsStorage().get(id) == null || userStorage.getUsersStorage().get(userId) == null) {
//            throw new StorageException("Не удалось удалить лайк. Ошибка поиска пользователя");
//        }
        String sql = "DELETE from likes where film_id = " + id +
                "and user_id =" + userId;
        jdbcTemplate.update(sql);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmsList = new ArrayList<>((filmDbStorage.getAll()));

        return filmsList.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1))
                .limit(count).collect(Collectors.toList());
    }
}
