package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storrage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storrage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")

public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    private final FilmDbStorage filmDbStorage;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        return filmDbStorage.add(film);
    }

    @PutMapping
    public Film refresh(@Valid @RequestBody Film film) {
        return filmDbStorage.refresh(film);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmDbStorage.getAll();
    }

    @GetMapping("/{id}")
    public Collection<Film> getFilmById(@PathVariable("id") Integer id) {
        return filmDbStorage.getFilmById(id); /* + */
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilm(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }
}


