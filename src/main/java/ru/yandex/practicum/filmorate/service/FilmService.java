package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storrage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    public FilmService(FilmDbStorage filmDbStorage, JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addLike(int id, int userId) {
        if (filmDbStorage.getFilmById(id) == null || userDbStorage.getUserById(id) == null) {
            throw new StorageException("Не удалось поставить лайк. Ошибка поиска пользователя");
        }
        String sql = String.format("INSERT INTO likes(film_id, user_id) VALUES ('%d','%d')", id, userId);
        jdbcTemplate.update(sql);
    }

    public void deleteLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }

        if (filmDbStorage.getFilmById(id) == null || userDbStorage.getUserById(id) == null) {
            throw new StorageException("Не удалось удалить лайк. Ошибка поиска пользователя");
        }
        String sql = String.format("DELETE from likes where film_id = %d and user_id =%d", id, userId);
        jdbcTemplate.update(sql);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmsList = new ArrayList<>((filmDbStorage.getAll()));

        return filmsList.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1))
                .limit(count).collect(Collectors.toList());
    }
}
