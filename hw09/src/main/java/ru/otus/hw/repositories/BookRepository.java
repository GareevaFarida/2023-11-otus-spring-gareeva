package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph("book-entity-graph-comments")
    Optional<Book> findById(long id);

    @Override
    @EntityGraph("book-entity-graph")
    List<Book> findAll();

    @EntityGraph("book-entity-graph")
    Optional<Book> findBookById(long id);
}
