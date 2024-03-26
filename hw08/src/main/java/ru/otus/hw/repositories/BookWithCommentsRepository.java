package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.BookWithComments;

import java.util.List;
import java.util.Optional;

public interface BookWithCommentsRepository extends MongoRepository<BookWithComments, String> {
    Optional<BookWithComments> getByBookId(String bookId);

    void deleteAllByGenreId(String genreId);

    void deleteAllByAuthorId(String authorId);

    void deleteByBookId(String bookId);

    List<BookWithComments> findAllByGenreId(String genreId);

    List<BookWithComments> findAllByAuthorId(String authorId);
}
