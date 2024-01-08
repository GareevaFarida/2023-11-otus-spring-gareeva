package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcNamed;

    @Override
    public List<Genre> findAll() {
        return jdbcNamed.query("select id, name from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        var genres = jdbcNamed.query("select id, name from genres where id=:id",
                new MapSqlParameterSource("id", id),
                new GenreRowMapper());
        if (genres.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(genres.get(0));
    }

    @Override
    public void deleteById(long id) {
        int deleted = jdbcNamed.update("delete from genres where id=:id",
                new MapSqlParameterSource("id", id));
        if (deleted == 0) {
            throw new EntityNotFoundException(getStringError(id));
        }
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0L) {
            return insert(genre);
        }
        return update(genre);
    }

    private Genre update(Genre genre) {
        int updated = jdbcNamed.update(
                "update genres set name =:name where id=:id",
                new MapSqlParameterSource("name", genre.getName())
                        .addValue("id", genre.getId()));
        if (updated == 0) {
            throw new EntityNotFoundException(getStringError(genre.getId()));
        }
        return genre;
    }

    private String getStringError(long id) {
        return "No one genre with id = %d was found".formatted(id);
    }

    private Genre insert(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcNamed.update("insert into genres (name) values (:name)",
                params, kh, new String[]{"id"});
        long genreId = kh.getKey().longValue();
        genre.setId(genreId);
        return genre;
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
