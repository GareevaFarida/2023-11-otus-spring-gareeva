package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getLongList")
    void shouldReturnCorrectBookById(long id) {
        var expectedBook = em.find(Book.class, id);
        var actualBook = repositoryJpa.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = em.find(Author.class, 1L);
        Assertions.assertThat(author).isNotNull();

        var genre = em.find(Genre.class, 1L);
        Assertions.assertThat(genre).isNotNull();

        var expectedBook = new Book(0, UUID.randomUUID().toString(), author, genre,
                Collections.emptyList());
        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять новую книгу c новыми комментариями и находить "
            + "ее по идентификатору книги со всеми комментариями")
    @Test
    void shouldFindBookWithComments() {
        var author = em.find(Author.class, 1L);
        Assertions.assertThat(author).isNotNull();

        var genre = em.find(Genre.class, 1L);
        Assertions.assertThat(genre).isNotNull();

        var expectedBook = new Book(0, UUID.randomUUID().toString(), author, genre,
                Collections.emptyList());
        List<Comment> comments = Arrays.asList(
                new Comment(0, "comment1", expectedBook),
                new Comment(0, "comment2", expectedBook),
                new Comment(0, "comment3", expectedBook)
        );
        expectedBook.setComments(comments);

        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        var booksWithComments = repositoryJpa.findByIdWithComments(returnedBook.getId());
        assertThat(booksWithComments)
                .isNotNull()
                .matches(book -> book.get().getComments().size() == 3);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var bookFromDb = em.find(Book.class, 1L);
        Assertions.assertThat(bookFromDb).isNotNull();
        var newTitle = UUID.randomUUID().toString();
        var bookForUpdate = new Book(bookFromDb.getId(),
                newTitle,
                bookFromDb.getAuthor(),
                bookFromDb.getGenre(),
                bookFromDb.getComments());
        var updatedBook = repositoryJpa.save(bookForUpdate);
        Assertions.assertThat(updatedBook).usingRecursiveComparison().isEqualTo(bookForUpdate);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, 1L)).isNotNull();
        repositoryJpa.deleteById(1L);
        assertThat(em.find(Book.class, 1L)).isNull();
    }

    private static List<Long> getLongList() {
        return LongStream.range(1, 16).boxed().toList();
    }
}
