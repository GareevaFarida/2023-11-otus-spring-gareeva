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
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Transactional
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbcNamed;

    @Override
    public List<Author> findAll() {
        return jdbcNamed.query("select id, full_name from authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var authors = jdbcNamed.query("select id, full_name from authors where id=:id",
                new MapSqlParameterSource("id", id),
                new AuthorRowMapper());
        if (authors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(authors.get(0));
    }

    @Override
    public void deleteById(long id) {
        int deleted = jdbcNamed.update("delete from authors where id=:id",
                new MapSqlParameterSource("id", id));
        if (deleted == 0) {
            throw new EntityNotFoundException(getStringError(id));
        }
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0L) {
            return insert(author);
        }
        return update(author);
    }

    private Author update(Author author) {
        int updated = jdbcNamed.update(
                "update authors set full_name =:name where id=:id",
                new MapSqlParameterSource("name", author.getFullName())
                        .addValue("id", author.getId()));
        if (updated == 0) {
            throw new EntityNotFoundException(getStringError(author.getId()));
        }
        return author;
    }

    private String getStringError(long id) {
        return "No one author with id = %d was found".formatted(id);
    }

    private Author insert(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", author.getFullName());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcNamed.update("insert into authors (full_name) values (:name)",
                params, kh, new String[]{"id"});
        long authorId = kh.getKey().longValue();
        author.setId(authorId);
        return author;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("full_name");
            return new Author(id, name);
        }
    }
}
