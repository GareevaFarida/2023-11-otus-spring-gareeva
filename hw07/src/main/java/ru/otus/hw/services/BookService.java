package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookWithCommentsDto> findByIdWithComments(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, long genreId);

    BookDto update(long id, String title, long authorId, long genreId);

    void deleteById(long id);
}
