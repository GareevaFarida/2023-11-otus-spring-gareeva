package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findAllCommentsByBookId(String bookId);

    CommentDto insert(String comment, String bookId);

    CommentDto update(String commentId, String comment);

    void deleteById(String id);
}
