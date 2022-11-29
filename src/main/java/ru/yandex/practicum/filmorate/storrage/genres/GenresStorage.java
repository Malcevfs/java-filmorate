package ru.yandex.practicum.filmorate.storrage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenresStorage {

    Genre add(Genre genre);

    Genre refresh(Genre genre);

    Collection<Genre> getAll();

    Genre getGenreById(Integer id);
}
