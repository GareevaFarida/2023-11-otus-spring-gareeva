package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookWithComments;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookWithCommentsRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookWithCommentsRepository bookWithCommentsRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookWithCommentsDto> findByIdWithComments(String id) {
        var bookWithCommentsOptional = bookWithCommentsRepository.getByBookId(id);
        if (bookWithCommentsOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found book with id = %s".formatted(id));
        }
        return bookWithCommentsOptional.map(bookWithComments ->
                modelMapper.map(bookWithComments, BookWithCommentsDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(String id) {
        var bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found book with id = %s".formatted(id));
        }
        return bookOptional.map(book ->
                modelMapper.map(book, BookDto.class));
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
        var bookWithIdOptional = bookWithCommentsRepository.getByBookId(bookId);
        bookWithIdOptional.ifPresent(bookWithCommentsRepository::delete);
        var comments = commentRepository.findAllByBookId(bookId);
        commentRepository.deleteAll(comments);
    }

    @Override
    public String addComment(String bookId, String comment) {
        var bookWithCommentsOptional = bookWithCommentsRepository.getByBookId(bookId);
        if (bookWithCommentsOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found book with id = %s".formatted(bookId));
        }
        var book = bookWithCommentsOptional.get();
        var commentEntity = new Comment(null, comment, bookId);
        var savedCommentEntity = commentRepository.insert(commentEntity);
        book.getComments().add(savedCommentEntity);
        bookWithCommentsRepository.save(book);
        return savedCommentEntity.getId();
    }

    private BookDto save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        var savedBook = bookRepository.save(book);
        if (isNull(id)) {
            var bookWithComment = new BookWithComments(null, savedBook.getId(), Collections.emptyList());
            bookWithCommentsRepository.save(bookWithComment);
        }
        return modelMapper.map(savedBook, BookDto.class);
    }
}
