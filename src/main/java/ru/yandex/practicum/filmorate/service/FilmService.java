package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storrage.film.FilmDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;


    public void addLike(int id, int userId) {
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        filmDbStorage.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(int count) {
       return filmDbStorage.getTopFilms(count);
    }
}
