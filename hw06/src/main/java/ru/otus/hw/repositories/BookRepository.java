package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(long id);

    Optional<Book> findByIdWithComments(long id);

    List<Book> findAll();

    Book save(Book book);

    void deleteById(long id);
}