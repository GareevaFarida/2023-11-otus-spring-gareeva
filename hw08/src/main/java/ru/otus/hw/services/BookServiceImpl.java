package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.bind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<BookWithCommentsDto> findByIdWithComments(String id) {
        var bookWithCommentsOptional = bookRepository.findById(id);
        if (bookWithCommentsOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found book with id = %s".formatted(id));
        }
        return bookWithCommentsOptional.map(bookWithComments ->
                modelMapper.map(bookWithComments, BookWithCommentsDto.class));
    }

    @Override
    public Optional<BookDto> findById(String id) {
        var bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found book with id = %s".formatted(id));
        }
        return bookOptional.map(book ->
                modelMapper.map(book, BookDto.class));
    }


    @Override
    public List<BookDto> findAll() {
        TypedAggregation<Book> aggregation = newAggregation(Book.class,
                project()
                        .and("title").as("title")
                        .and("author")
                        .nested(bind("_id", "author.id").and(bind("fullName", "author.fullName")))
                        .and("genre").nested(bind("_id", "genre.id").and(bind("name", "genre.name")))
        );
        List<Book> books = mongoTemplate.aggregate(aggregation, Book.class).getMappedResults();
        return books
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }

    @Override
    public BookDto insert(String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new Book(null, title, author, genre, Collections.emptyList());
        var savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDto.class);
    }

    @Override
    public BookDto update(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new Book(id, title, author, genre, Collections.emptyList());

        Update update = new Update().set("title", title).set("author", author).set("genre", genre);
        mongoTemplate.update(Book.class)
                .matching(Criteria.where("_id").is(id))
                .apply(update).first();
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public void deleteById(String bookId) {
        bookRepository.deleteById(bookId);
    }

}
