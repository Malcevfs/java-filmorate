package ru.yandex.practicum.filmorate.storrage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Component
@Qualifier("filmDbStorage")
public interface FilmStorage {

    Film add(Film film);

    Film refresh(Film film);

    Collection<Film> getFilmById(int id);

    List<Film> getAll();

}


