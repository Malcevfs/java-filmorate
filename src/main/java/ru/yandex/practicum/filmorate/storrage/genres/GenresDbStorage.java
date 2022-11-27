package ru.yandex.practicum.filmorate.storrage.genres;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@Primary
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAll() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Collection<Genre> getGenreById(Integer id) {
        if (!checkGenreId(id)) {
            throw new StorageException("Genre с таким Id нет в хранилище");
        }
        String sql = "select * from genre where genre_id =" + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre");
        Genre genre = new Genre(genreId, genreName);
        return genre;
    }

    public boolean checkGenreId(int id) {
        boolean check = false;
        String sql = "select * from genre " +
                "where GENRE_ID =" + id;
        List<Genre> genere = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
        if (!genere.isEmpty()) {
            check = true;
        }
        return check;
    }
}
