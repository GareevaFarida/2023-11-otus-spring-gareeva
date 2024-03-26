package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.CommentDto;

import java.util.UUID;

import static java.util.Objects.nonNull;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Получает список всех книг")
    @Test
    public void findAllTest() {
        var books = bookService.findAll();
        Assertions.assertThat(books)
                .matches(list -> list.size() == 2);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("Сохраняет новую книгу")
    @Test
    public void insertTest() {
        var bookList = bookService.findAll();
        Assertions.assertThat(bookList)
                .isNotEmpty();
        var book = bookList.get(0);
        var author = book.getAuthor();
        var genre = book.getGenre();
        var title = "Новая книга";
        var insertedBook = bookService.insert(title, author.getId(), genre.getId());
        var bookListAfterInsertion = bookService.findAll();
        Assertions.assertThat(bookListAfterInsertion)
                .matches(list -> list.size() - bookList.size() == 1);
        Assertions.assertThat(insertedBook)
                .isNotNull()
                .matches(b -> title.equals(b.getTitle()))
                .matches(b -> nonNull(b.getAuthor()))
                .matches(b -> b.getAuthor().equals(author))
                .matches(b -> nonNull(b.getGenre()))
                .matches(b -> b.getGenre().equals(genre));
    }

    @DisplayName("Находит книгу по идентификатору")
    @Test
    public void findByIdTest() {
        var bookList = bookService.findAll();
        Assertions.assertThat(bookList)
                .matches(list -> list.size() == 2);
        var book = bookList.get(0);
        var foundBook = bookService.findById(book.getId());
        Assertions.assertThat(foundBook)
                .isNotNull();
        Assertions.assertThat(foundBook.get())
                .usingRecursiveComparison().isEqualTo(book);
    }

    @DisplayName("Обновляет книгу")
    @Test
    public void updateTest() {
        var bookList = bookService.findAll();
        Assertions.assertThat(bookList)
                .matches(list -> list.size() == 2);
        var book = bookList.get(0);
        String title = UUID.randomUUID().toString();
        var updatedBook = bookService.update(book.getId(),
                title,
                book.getAuthor().getId(),
                book.getGenre().getId());
        Assertions.assertThat(updatedBook)
                .isNotNull()
                .matches(b -> title.equals(b.getTitle()));
    }

    @DisplayName("Удаляет книгу по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void deleteByIdTest() {
        var bookList = bookService.findAll();
        Assertions.assertThat(bookList)
                .matches(list -> list.size() == 2);
        var book = bookList.get(0);
        bookService.deleteById(book.getId());
        var bookListAfterDelete = bookService.findAll();
        Assertions.assertThat(bookListAfterDelete)
                .matches(list -> bookList.size() - list.size() == 1);
    }

    @DisplayName("Находит книгу по идентификатору со всеми ее комментариями")
    @Test
    public void findByIdWithCommentsTest() {
        var bookList = bookService.findAll();
        Assertions.assertThat(bookList)
                .matches(list -> list.size() == 2);
        var book = bookList.get(0);

        var bookWithComments = bookService.findByIdWithComments(book.getId());

        Assertions.assertThat(bookWithComments)
                .isNotNull()
                .isNotEmpty()
                .matches(b -> !b.get().getComments().isEmpty());
    }

    @DisplayName("Находит книгу по идентификатору со всеми ее комментариями")
    @Test
    public void addCommentTest() {
        var bookList = bookService.findAll();
        Assertions.assertThat(bookList)
                .matches(list -> list.size() == 2);
        var book = bookList.get(0);

        var bookWithCommentsOptional = bookService.findByIdWithComments(book.getId());

        Assertions.assertThat(bookWithCommentsOptional)
                .isNotNull()
                .isNotEmpty()
                .matches(b -> !b.get().getComments().isEmpty());
        var bookWithComments = bookWithCommentsOptional.get();
        String newComment = UUID.randomUUID().toString();
        var newCommentId = bookService.addComment(bookWithComments.getBookId(), newComment);
        var bookWithCommentsAfterAddingCommentOptional = bookService.findByIdWithComments(book.getId());
        Assertions.assertThat(bookWithCommentsAfterAddingCommentOptional)
                .isNotNull()
                .isNotEmpty();
        var bookWithCommentsAfterAddingComment = bookWithCommentsAfterAddingCommentOptional.get();
        Assertions.assertThat(bookWithCommentsAfterAddingComment.getComments())
                .isNotEmpty()
                .matches(list -> list.size() - bookWithComments.getComments().size() == 1)
                .matches(list -> list.stream()
                        .map(CommentDto::getId)
                        .filter(c -> c.equals(newCommentId))
                        .count() == 1);
    }
}
