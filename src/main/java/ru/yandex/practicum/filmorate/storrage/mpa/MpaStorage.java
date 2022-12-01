package ru.yandex.practicum.filmorate.storrage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Collection;

public interface MpaStorage {

    Collection<Mpa> getAll();

    Mpa getMpaById(Integer id);
}