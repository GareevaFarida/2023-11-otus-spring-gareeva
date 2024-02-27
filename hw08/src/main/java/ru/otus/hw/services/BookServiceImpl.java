package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookWithCommentsDto> findByIdWithComments(String id) {
        return bookRepository.findById(id)
                .map(book -> modelMapper.map(book, BookWithCommentsDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    @Transactional
    public BookDto update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    @Transactional
    public void deleteById(String bookId) {
        var bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            return;
        }
        bookRepository.deleteById(bookId);
        var comments = commentRepository.findAllByBookId(bookId);
        commentRepository.deleteAll(comments);
    }

    @Override
    public String addComment(String bookId, String comment) {
        var bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found book with id = %s".formatted(bookId));
        }
        var book = bookOptional.get();
        var commentEntity = new Comment(null, comment, bookId);
        var savedCommentEntity = commentRepository.insert(commentEntity);
        book.getComments().add(savedCommentEntity);
        bookRepository.save(book);
        return savedCommentEntity.getId();
    }

    private BookDto save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new Book(id, title, author, genre, Collections.emptyList());
        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDto.class);
    }
}
