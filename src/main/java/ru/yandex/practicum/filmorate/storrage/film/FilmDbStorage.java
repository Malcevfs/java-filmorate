package ru.yandex.practicum.filmorate.storrage.film;

import lombok.RequiredArgsConstructor;
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
import ru.yandex.practicum.filmorate.storrage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    @Override
    public Film add(Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше выхода первого фильма");
        }

        String sql = "INSERT INTO films (name,description,release_date,duration,rate, mpa_id) VALUES " +
                "('" + film.getName() + "'" + "," + "'" + film.getDescription() + "'" + "," + "'" +
                film.getReleaseDate() + "'" + "," + "'" + film.getDuration() + "'" + "," + "'" +
                film.getRate() + "'" + "," + "'" + film.getMpa().getId() + "')";

        jdbcTemplate.update(sql);

        String sql1 = "select * from mpa " +
                "where mpa_id=" + film.getMpa().getId();
        List<String> mpa = jdbcTemplate.query(sql1, (rs, rowNum) -> getMpa(rs));

        String sql2 = "select FILM_ID, F.NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, m.MPA_ID, M.NAME as mpa_name " +
                "from FILMS F\n" +
                "join MPA M on F.MPA_ID = M.MPA_ID\n" +
                "and F.NAME= '" + film.getName() + "'";
        List<Film> film2 = jdbcTemplate.query(sql2, (rs, rowNum) -> makeFilm(rs));

        film.setId(film2.get(0).getId());

        String sql3 = "select *  from FILM_GENRE\n" +
                "join GENRE G2 on G2.GENRE_ID = FILM_GENRE.GENRE_ID\n" +
                "and FILM_ID=" + film.getId();
        List<Genre> genres = jdbcTemplate.query(sql3, (rs, rowNum) -> getGenreForSet(rs));

        if (genres.isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String sql4 = "INSERT INTO film_genre (film_id, genre_id)" +
                        " VALUES ('" + film.getId() + "'" + "," + "'" + genre.getId() + "')";
                jdbcTemplate.update(sql4);
            }
        }

        film.getMpa().setName(mpa.get(0));
        log.info("Фильм с id " + film.getId() + " добавлен в хранилище");
        return film;
    }

    @Override
    public Film refresh(Film film) {
        if (checkFilmId(film.getId())) {
            throw new StorageException("Фильма с таким id не существует в базе");
        }

        String sql = "update films set " +
                "name ='" + film.getName() + "'" + "," +
                "description ='" + film.getDescription() + "'" + "," +
                "release_date ='" + film.getReleaseDate() + "'" + "," +
                "duration ='" + film.getDuration() + "'" + "," +
                "rate ='" + film.getRate() + "'" + "," +
                "mpa_id ='" + film.getMpa().getId() + "'" +
                "where film_id=" + film.getId();

        jdbcTemplate.update(sql);

        String sql1 = "DELETE FROM film_genre where FILM_ID =" + film.getId();
        jdbcTemplate.update(sql1);

        for (Genre genre : film.getGenres()) {
            String sql2 = "INSERT INTO film_genre (film_id, genre_id) " +
                    "VALUES ('" + film.getId() + "'" + "," + "'" + genre.getId() + "')";

            jdbcTemplate.update(sql2);
        }
        log.info("Фильм " + film.getName() + " с id " + film.getId() + " обновлен в хранилище");
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        if (checkFilmId(id)) {
            throw new StorageException("Фильма с таким id не существует в базе");
        }

        String sql = "select FILM_ID, F.NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, m.MPA_ID, M.NAME as mpa_name" +
                " from FILMS F\n" +
                "join MPA M on F.MPA_ID = M.MPA_ID\n" +
                "and F.FILM_ID = " + id;
        List<Film> film = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        String sql1 = "select * from LIKES where FILM_ID =" + id;
        List<Integer> likes = jdbcTemplate.query(sql1, (rs, rowNum) -> getLikes(rs));
        for (Integer userId : likes) {
            film.get(0).getLikes().add(Long.valueOf(userId));
        }

        String sql2 = "select *  from FILM_GENRE\n" +
                "join GENRE G2 on G2.GENRE_ID = FILM_GENRE.GENRE_ID\n" +
                "and FILM_ID=" + id +
                " ORDER BY GENRE_ID";
        List<Genre> genres = jdbcTemplate.query(sql2, (rs, rowNum) -> getGenreForSet(rs));
        for (Genre genre : genres) {
            film.get(0).getGenres().add(genre);
        }
        log.info("Выполнен запрос поиска фильма " + film.get(0).getName() + " с id " + id);
        return film.get(0);
    }

    @Override
    public List<Film> getAll() {
        List<Film> film1 = new ArrayList<>();
        String sql = "select F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATE, M.MPA_id, M.NAME as mpa_name " +
                " from FILMS F\n" +
                "join MPA M on F.MPA_ID = M.MPA_ID\n";
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
            film1.add(getFilmById(i));
        }
        log.info("Выполнен запрос списка всех фильмов");
        return film1;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        long rate = rs.getLong("rate");
        Film film = new Film(name, description, release_date, duration);
        film.setId(id);
        film.setRate(rate);
        Mpa mpa = new Mpa(mpaId, mpaName);
        film.setMpa(mpa);
        return film;
    }

    private String getMpa(ResultSet rs) throws SQLException {

        return rs.getString("name");
    }

    private Genre getGenreForSet(ResultSet rs) throws SQLException {
        int genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre");

        return new Genre(genreId, genreName);
    }

    private Integer getLikes(ResultSet rs) throws SQLException {

        return rs.getInt("user_id");
    }

    public boolean checkFilmId(int id) {
        boolean check = false;
        String sql = "select FILM_ID, F.NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, m.MPA_ID, M.NAME as mpa_name " +
                "from FILMS F\n" +
                "join MPA M on F.MPA_ID = M.MPA_ID\n" +
                "and F.FILM_ID = " + id;
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (!films.isEmpty()) {
            check = true;
        }
        return !check;
    }

    public void addLike(int id, int userId) {
        if (getFilmById(id) == null || userDbStorage.getUserById(id) == null) {
            throw new StorageException("Не удалось поставить лайк. Ошибка поиска пользователя");
        }
        String sql = String.format("INSERT INTO likes(film_id, user_id) VALUES ('%d','%d')", id, userId);
        jdbcTemplate.update(sql);
    }

    public void deleteLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new StorageException("Значение Id не может быть отрицательным");
        }

        if (getFilmById(id) == null || userDbStorage.getUserById(id) == null) {
            throw new StorageException("Не удалось удалить лайк. Ошибка поиска пользователя");
        }
        String sql = String.format("DELETE from likes where film_id = %d and user_id =%d", id, userId);
        jdbcTemplate.update(sql);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmsList = new ArrayList<>((getAll()));

        return filmsList.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1))
                .limit(count).collect(Collectors.toList());
    }

}

