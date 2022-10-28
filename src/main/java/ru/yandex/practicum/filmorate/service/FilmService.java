package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
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

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int id, int userId) {
        if (filmStorage.getFilmsStorage().get(id) == null || userStorage.getUsersStorage().get(userId) == null) {
            throw new StorageException("Не удалось поставить лайк. Ошибка поиска пользователя");
        }
        filmStorage.getFilmsStorage().get(id).getLikes().add((long) userId);
    }

    public void deleteLike(int id, int userId) {
        if (filmStorage.getFilmsStorage().get(id) == null || userStorage.getUsersStorage().get(userId) == null) {
            throw new StorageException("Не удалось удалить лайк. Ошибка поиска пользователя");
        }
        filmStorage.getFilmsStorage().get(id).getLikes().remove((long) userId);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmsList = new ArrayList<>((filmStorage.getFilmsStorage().values()));

        return filmsList.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1)).limit(count).collect(Collectors.toList());
    }
}
