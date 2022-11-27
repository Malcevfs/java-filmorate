package ru.yandex.practicum.filmorate.storrage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    protected int id = 0;
    private final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше выхода первого фильма");
        }

        System.out.println(film);
        String sql = "INSERT INTO films (name,description,release_date,duration) VALUES ('" + film.getName() + "'" + "," + "'" + film.getDescription() + "'" + "," + "'" + film.getReleaseDate() + "'" + "," + "'" + film.getDuration() + "'" + ")";

        jdbcTemplate.update(sql);
        String sql2 = "select * from genre " +
                "where genre_id=" + film.getMpa().getId();
        List<String> mpa = jdbcTemplate.query(sql2, (rs, rowNum) -> getGenre(rs));
        film.getMpa().setMpa(mpa.get(0));
        log.info("Фильм с id" + film.getId() + " добавлен в хранилище");
        return film;
    }

    @Override
    public Film refresh(Film film) {
        if (!checkFilmId(film.getId())) {
            throw new StorageException("Пользователя с таким id не существует в базе");
        }

        String sql = "update films set " +
                "name ='" + film.getName() + "'" + "," +
                "description ='" + film.getDescription() + "'" + "," +
                "release_date ='" + film.getReleaseDate() + "'" + "," +
                "duration ='" + film.getDuration() + "'" +
                "where film_id=" + film.getId();
        jdbcTemplate.update(sql);
        return film;
    }

    @Override
    public Collection<Film> getFilmById(int id) {

        String sql = "select FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, m.MPA_ID, mpa from FILMS\n" +
                "join MPA M on FILMS.MPA_ID = M.MPA_ID\n" +
                "and FILM_ID = " + id;
        List<Film> film = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        String sql1 = "select * from LIKES where FILM_ID =" + id;
        List<Integer> likes = jdbcTemplate.query(sql1, (rs, rowNum) -> getLikes(rs));
        for (Integer userId : likes) {
            film.get(0).getLikes().add(Long.valueOf(userId));
        }

        String sql2 = "select *  from FILM_GENRE\n" +
                "join GENRE G2 on G2.GENRE_ID = FILM_GENRE.GENRE_ID\n" +
                "and FILM_ID=" + id;
        List<Genre> genres = jdbcTemplate.query(sql2, (rs, rowNum) -> getGenreForSet(rs));
        for (Genre genre : genres) {
            film.get(0).getGenre().add(genre);
        }


        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> film1 = new ArrayList<>();
        String sql = "select FG.FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, GENRE, G2.GENRE_ID, M.MPA_id, MPA from FILMS\n" +
                "join FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID\n" +
                "join MPA M on FILMS.MPA_ID = M.MPA_ID\n" +
                "join GENRE G2 on G2.GENRE_ID = FG.GENRE_ID";
        List<Film> film = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        /** Не получалось получить корректные значения лайков для всех фильмов. Путаются значения в листах. Решил пока сделать
         через метод получения фильма по id **/

//        for(int i = 0; i <= film.size() - 1; i++) {
//           int filmId = film.get(i).getId();
//            String sql1 = "select * from LIKES where FILM_ID =" + (filmId);
//            List<Integer> likes = jdbcTemplate.query(sql1, (rs, rowNum) -> getLikes(rs));
//            for (Integer userId : likes) {
//
//                    film.get(filmId).getLikes().add(Long.valueOf(userId));
//
//            }
//        }

        for (int i = 1; i <= film.size(); i++) {
            film1.addAll(getFilmById(i));
        }
        return film1;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa");
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Film film = new Film(name, description, release_date, duration);
        film.setId(id);
        Mpa mpa = new Mpa(mpaId, mpaName);
        film.setMpa(mpa);

        return film;
    }

    private String getGenre(ResultSet rs) throws SQLException {
        String genre = rs.getString("genre");

        return genre;
    }

    private Genre getGenreForSet(ResultSet rs) throws SQLException {
        int genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre");
        Genre genre = new Genre(genreId, genreName);

        return genre;
    }


    private Integer getLikes(ResultSet rs) throws SQLException {
        int like = rs.getInt("user_id");

        return like;
    }

    public boolean checkFilmId(int id) {
        boolean check = false;
        String sql = "select * from FILMS " +
                "where film_id =" + id;
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (!films.isEmpty()) {
            check = true;
        }
        return check;
    }

}

