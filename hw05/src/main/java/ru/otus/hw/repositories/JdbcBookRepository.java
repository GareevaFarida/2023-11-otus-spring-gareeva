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
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcNamed;

    @Override
    public Optional<Book> findById(long id) {
        List<Book> books = jdbcNamed.query(
                """
                        select 
                            books.id as book_id, 
                            books.title as book_title, 
                            books.author_id, 
                            books.genre_id,
                            authors.full_name as  authors_full_name,
                            genres.name as genres_name
                        from books
                        join authors on authors.id = books.author_id
                        join genres on genres.id = books.genre_id
                        where books.id=:id
                        """,
                new MapSqlParameterSource("id", id),
                new BookRowMapper());
        if (books.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(books.get(0));
    }

    @Override
    public List<Book> findAll() {
        return jdbcNamed.query(
                """
                        select 
                            books.id as book_id, 
                            books.title as book_title, 
                            books.author_id, 
                            books.genre_id,
                            authors.full_name as  authors_full_name,
                            genres.name as genres_name
                        from books
                        join authors on authors.id = books.author_id
                        join genres on genres.id = books.genre_id
                        """,
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        int deleted = jdbcNamed.update("delete from books where id=:id",
                new MapSqlParameterSource("id", id));
        if (deleted == 0) {
            throw new EntityNotFoundException(getStringError(id));
        }
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcNamed.update("insert into books (title,author_id,genre_id) values (:title,:author_id,:genre_id)",
                params, kh, new String[]{"id"});
        long bookId = kh.getKey().longValue();
        book.setId(bookId);
        return book;
    }

    private Book update(Book book) {
        int updated = jdbcNamed.update(
                "update books set title=:title,author_id=:author_id,genre_id=:genre_id where id=:id",
                new MapSqlParameterSource("title", book.getTitle())
                        .addValue("author_id", book.getAuthor().getId())
                        .addValue("genre_id", book.getGenre().getId())
                        .addValue("id", book.getId()));
        if (updated == 0) {
            throw new EntityNotFoundException(getStringError(book.getId()));
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = getAuthor(rs);
            Genre genre = getGenre(rs);
            return getBook(rs, author, genre);
        }
    }

    private static Book getBook(ResultSet rs, Author author, Genre genre) throws SQLException {
        long bookId = rs.getLong("book_id");
        String bookTitle = rs.getString("book_title");
        return new Book(bookId, bookTitle, author, genre);
    }

    private static Genre getGenre(ResultSet rs) throws SQLException {
        long genreId = rs.getLong("genre_id");
        String genreName = rs.getString("genres_name");
        return new Genre(genreId, genreName);
    }

    private static Author getAuthor(ResultSet rs) throws SQLException {
        long authorId = rs.getLong("author_id");
        String authorFullName = rs.getString("authors_full_name");
        return new Author(authorId, authorFullName);
    }

    private String getStringError(long id) {
        return "No one book with id = %d was found".formatted(id);
    }
}
