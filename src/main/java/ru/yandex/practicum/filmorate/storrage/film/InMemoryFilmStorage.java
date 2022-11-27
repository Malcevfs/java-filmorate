package ru.yandex.practicum.filmorate.storrage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected int id = 0;
    private final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final HashMap<Integer, Film> filmsStorage = new HashMap<>();

    @Override
    public Film add(Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше выхода первого фильма");
        }
        id++;
        film.setId(id);

        filmsStorage.put(film.getId(), film);
        log.info("Фильм с id" + film.getId() + " добавлен в хранилище");
        return film;
    }

    @Override
    public Film refresh(Film film) {
        for (Film films : filmsStorage.values()) {
            if (films.getId() != film.getId()) {
                throw new StorageException("Фильма с таким id не существует в базе");
            }
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше выхода первого фильма");

        }
        filmsStorage.put(film.getId(), film);
        log.info("Фильм изменен в хранилище c id " + film.getId());
        return film;
    }

    @Override
    public Collection<Film> getFilmById(int id) {
        if (filmsStorage.get(id) == null) {
            throw new StorageException("Фильма с таким Id нет в хранилище");
        }
        return (Collection<Film>) filmsStorage.get(id);
    }

    @Override
    public List<Film> getAll() {
        return null;
    }

    public HashMap<Integer, Film> getFilmsStorage() {
        return filmsStorage;
    }
}

