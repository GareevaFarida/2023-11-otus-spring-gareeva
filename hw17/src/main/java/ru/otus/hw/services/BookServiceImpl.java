package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

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
        List<Book> books = bookRepository.findAll();
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
        var book = new Book(null, title, author, genre, new Timestamp(System.currentTimeMillis()),
                Collections.emptyList());
        var savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDto.class);
    }

    @Override
    public BookDto update(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new Book(id, title, author, genre, new Timestamp(System.currentTimeMillis()),
                Collections.emptyList());

        bookRepository.saveBookIgnoreComments(book);
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public void deleteById(String bookId) {
        bookRepository.deleteById(bookId);
    }

}
