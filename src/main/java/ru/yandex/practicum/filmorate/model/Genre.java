package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    protected int id;
    protected String name;

    public Genre(int id, String genre) {
        this.id = id;
        this.name = genre;
    }
}
