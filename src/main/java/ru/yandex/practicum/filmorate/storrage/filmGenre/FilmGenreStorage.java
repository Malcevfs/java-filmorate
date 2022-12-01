package ru.yandex.practicum.filmorate.storrage.filmGenre;

public interface FilmGenreStorage {

    void add(int filmId, int genreId);

    void delete(int filmId);
}
