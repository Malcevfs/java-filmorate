package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storrage.genres.GenresDbStorage;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenresDbStorage genresDbStorage;

    @GetMapping
    public Collection<Genre> getAll() {
        return genresDbStorage.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") Integer id) {
        return genresDbStorage.getGenreById(id);
    }
    @PostMapping
    public Genre add(@RequestBody Genre genre) {
        return genresDbStorage.add(genre);
    }

    @PutMapping
    public Genre refresh(@RequestBody Genre genre) {
        return genresDbStorage.refresh(genre);
    }

}
