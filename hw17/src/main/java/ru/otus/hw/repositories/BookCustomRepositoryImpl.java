package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.bind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Book> findAll() {
        TypedAggregation<Book> aggregation = newAggregation(Book.class,
                project()
                        .and("title").as("title")
                        .and("author")
                        .nested(bind("_id", "author.id").and(bind("fullName", "author.fullName")))
                        .and("genre").nested(bind("_id", "genre.id").and(bind("name", "genre.name")))
        );
        return mongoTemplate.aggregate(aggregation, Book.class).getMappedResults();
    }

    @Override
    public void saveBookIgnoreComments(Book book) {
        Update update = new Update()
                .set("title", book.getTitle())
                .set("author", book.getAuthor())
                .set("genre", book.getGenre());
        mongoTemplate.update(Book.class)
                .matching(Criteria.where("_id").is(book.getId()))
                .apply(update).first();
    }

    @Override
    public void updateAuthorInfoInAllBooks(Author author) {
        Query query = new Query();
        query.addCriteria(Criteria.where("author._id").is(author.getId()));
        Update update = Update.update("author.fullName", author.getFullName());
        mongoTemplate.updateMulti(query, update, Book.class);
    }

    @Override
    public void updateGenreInfoInAllBooks(Genre genre) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre._id").is(genre.getId()));
        Update update = Update.update("genre.name", genre.getName());
        mongoTemplate.updateMulti(query, update, Book.class);
    }
}
