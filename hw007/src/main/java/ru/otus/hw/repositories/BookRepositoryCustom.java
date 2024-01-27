package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<Book> findById(long id);

    Optional<Book> findByIdWithComments(long id);
}
