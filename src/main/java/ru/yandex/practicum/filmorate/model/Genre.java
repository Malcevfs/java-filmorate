package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    int id;
    String genre;

    public Genre(int id, String genre) {
        this.id = id;
        this.genre = genre;
    }
}
