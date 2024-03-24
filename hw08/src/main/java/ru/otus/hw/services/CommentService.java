package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String bookId, String commentId);

    List<CommentDto> findAllCommentsByBookId(String bookId);

    CommentDto update(String bookId, String commentId, String comment);

    void deleteById(String bookId, String commentId);
}
