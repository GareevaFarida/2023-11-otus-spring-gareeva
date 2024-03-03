package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookWithCommentsDto> findByIdWithComments(String id);

    Optional<BookDto> findById(String id);

    List<BookDto> findAll();

    BookDto insert(String title, String authorId, String genreId);

    BookDto update(String id, String title, String authorId, String genreId);

    void deleteById(String id);

    String addComment(String bookId, String comment);
}
