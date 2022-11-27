package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Collection<Genre> getGenreById(@PathVariable("id") Integer id) {
        return genresDbStorage.getGenreById(id);
    }
}
