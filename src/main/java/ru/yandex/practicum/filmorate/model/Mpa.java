package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    long id;
    String mpa;

    public Mpa(long id, String mpa) {
        this.id = id;
        this.mpa = mpa;
    }
}
