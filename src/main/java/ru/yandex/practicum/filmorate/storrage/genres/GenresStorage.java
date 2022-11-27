package ru.yandex.practicum.filmorate.storrage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenresStorage {

    public Collection<Genre> getAll();

    public Collection<Genre> getGenreById(Integer id);
}
