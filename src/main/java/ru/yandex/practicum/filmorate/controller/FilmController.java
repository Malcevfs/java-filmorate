package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    protected int id = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> filmsStorage = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        createId();
        film.setId(id);

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше выхода первого фильма");
        }

        filmsStorage.put(film.getId(), film);
        log.info("Фильм с id" + film.getId() + " добавлен в хранилище");
        return film;
    }

    @PutMapping
    public Film refreshFilm(@Valid @RequestBody Film film) {
        for (Film films : filmsStorage.values()) {
            if (films.getId() != film.getId()) {
                throw new ValidationException("Фильма с таким id не существует в базе");
            }
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше выхода первого фильма");

        }

        filmsStorage.put(film.getId(), film);
        log.info("Фильм изменен в хранилище c id " + film.getId());
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmsStorage.values();
    }

    public int createId() {
        return id++;
    }
}

