package ru.otus.hw.repositories;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "comments")
public interface CommentRepository {
    Optional<Comment> findById(String bookId, String commentId);

    List<Comment> findAllCommentsByBookId(String bookId);

    Comment insert(String bookId, String comment);

    Comment update(String bookId, String commentId, String comment);

    void deleteById(String bookId, String commentId);
}
