package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.Comment;

import java.util.UUID;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private BookServiceImpl bookService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("Добавляет комментарий в книгу")
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
        var newCommentInserted = commentService.insert(bookWithComments.getId(), newComment);
        var bookWithCommentsAfterAddingCommentOptional = bookService.findByIdWithComments(book.getId());
        Assertions.assertThat(bookWithCommentsAfterAddingCommentOptional)
                .isNotNull()
                .isNotEmpty();
        var bookWithCommentsAfterAddingComment = bookWithCommentsAfterAddingCommentOptional.get();
        Assertions.assertThat(bookWithCommentsAfterAddingComment.getComments())
                .isNotEmpty()
                .matches(list -> list.size() - bookWithComments.getComments().size() == 1)
                .matches(list ->
                    list.stream()
                            .map(Comment::getId)
                            .filter(c -> c.equals(newCommentInserted.getId()))
                            .count()==1
                );
    }
}
