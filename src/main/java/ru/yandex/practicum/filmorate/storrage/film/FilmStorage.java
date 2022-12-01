package ru.yandex.practicum.filmorate.storrage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;


public interface FilmStorage {

    Film add(Film film);
    Film refresh(Film film);
    Film getFilmById(int id);
    List<Film> getAll();

}


