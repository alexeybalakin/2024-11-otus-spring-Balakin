package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    public JdbcGenreRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select id, name from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        Map<String, Object> params = Map.of("id", id);
        try {
            var result = jdbc.queryForObject(
            "select id, name from genres where id = :id", params, new GenreRowMapper());
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
