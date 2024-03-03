package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.BookWithComments;

import java.util.Optional;

public interface BookWithCommentsRepository extends MongoRepository<BookWithComments, String> {
    Optional<BookWithComments> getByBookId(String bookId);
}
