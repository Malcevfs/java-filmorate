package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storrage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storrage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        return filmStorage.add(film);
    }

    @PutMapping
    public Film refresh(@Valid @RequestBody Film film) {
        return filmStorage.refresh(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }
}


