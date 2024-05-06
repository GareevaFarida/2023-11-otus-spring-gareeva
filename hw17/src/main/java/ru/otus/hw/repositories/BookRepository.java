package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Book;
import ru.otus.hw.projections.BookProjection;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(excerptProjection = BookProjection.class)
public interface BookRepository extends MongoRepository<Book, String>, BookCustomRepository {

    void deleteAllByAuthor_Id(String authorId);

    void deleteAllByGenre_Id(String genreId);

    @RestResource(path = "title", rel = "title")
    List<Book> findAllByTitle(String title);

    int countAllByCreatedAfter(Date created);
}
