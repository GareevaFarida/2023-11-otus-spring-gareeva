package ru.otus.hw.services;

import ru.otus.hw.dto.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(String bookId, String commentId);

    List<Comment> findAllCommentsByBookId(String bookId);

    Comment insert(String bookId, String comment);

    void deleteById(String bookId, String commentId);
}
