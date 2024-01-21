package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
@Import({BookRepositoryJpa.class})
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getIntArray")
    void shouldReturnCorrectBookById(int id) {
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

    @Test
    void shoudReturnCommentsListAfterDetached(){
        var book = em.find(Book.class, 1L);
        em.detach(book);
        System.out.println(book.getAuthor().getFullName());
        //System.out.println(book.getComments().size());
    }

    private static List<Integer> getIntArray() {
        return IntStream.range(1, 16).boxed().toList();
    }
}
