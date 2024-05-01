package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface BookCustomRepository {
    List<Book> findAll();

    void saveBookIgnoreComments(Book book);

    void updateAuthorInfoInAllBooks(Author author);

    void updateGenreInfoInAllBooks(Genre genre);
}
