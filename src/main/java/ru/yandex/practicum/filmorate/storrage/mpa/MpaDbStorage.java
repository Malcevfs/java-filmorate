package ru.yandex.practicum.filmorate.storrage.mpa;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@Primary
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAll() {
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Collection<Mpa> getMpaById(Integer id) {
        if (!checkMpaId(id)) {
            throw new StorageException("Mpa с таким Id нет в хранилище");
        }
        String sql = "select * from mpa where mpa_id =" + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa");
        Mpa mpa = new Mpa(mpaId, mpaName);
        return mpa;
    }

    public boolean checkMpaId(int id) {
        boolean check = false;
        String sql = "select * from mpa " +
                "where MPA_ID =" + id;
        List<Mpa> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
        if (!mpa.isEmpty()) {
            check = true;
        }
        return check;
    }

}
