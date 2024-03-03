package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findAllByGenre_Id(String genreId);

    List<Book> findAllByGenre(Genre genre);

    List<Book> findAllByAuthor_Id(String authorId);
}
