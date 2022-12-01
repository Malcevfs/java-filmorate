package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    protected long id;
    protected String name;

    public Mpa(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
