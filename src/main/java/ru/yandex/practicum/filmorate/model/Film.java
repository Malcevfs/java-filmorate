package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    protected int id;
    @NotBlank(message = "empty name")
    protected String name;
    @Size(max = 200, message = "max description size is 200 chars")
    protected String description;
    protected LocalDate releaseDate;
    @Positive(message = "duration is negative")
    protected int duration;
    protected Set<Long> likes = new HashSet<>();
    Set<Genre> genres = new HashSet<>();
    protected long rate;
    protected Mpa mpa;

}
