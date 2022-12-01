package ru.yandex.practicum.filmorate.storrage.filmGenre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(int filmId, int genreId) {
        String sql4 = String.format("INSERT INTO film_genre (film_id, genre_id) VALUES ('%d','%d')", filmId, genreId);
        jdbcTemplate.update(sql4);
    }

    @Override
    public void delete(int filmId) {
        String sql1 = String.format("DELETE FROM film_genre where FILM_ID =%d", filmId);
        jdbcTemplate.update(sql1);

    }
}
