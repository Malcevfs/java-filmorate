package ru.yandex.practicum.filmorate.storrage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {

    public Collection<Mpa> getAll();

    public Collection<Mpa> getMpaById(Integer id);
}