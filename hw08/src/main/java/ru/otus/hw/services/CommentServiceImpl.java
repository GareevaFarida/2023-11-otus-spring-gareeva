package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .map(val -> modelMapper.map(val, CommentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllCommentsByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(val -> modelMapper.map(val, CommentDto.class))
                .toList();
    }

    @Override
    public CommentDto insert(String comment, String bookId) {
        var bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Комментарий %s ссылается на книгу с id = %s, которой нет"
                    .formatted(comment, bookId));
        }
        var entity = new Comment(null, comment, bookId);
        var savedEntity = commentRepository.save(entity);
        var book = bookOptional.get();
        book.getComments().add(savedEntity);
        bookRepository.save(book);
        return modelMapper.map(savedEntity, CommentDto.class);
    }

    @Override
    @Transactional
    public CommentDto update(String commentId, String commentText) {
        var commentEntityOptional = commentRepository.findById(commentId);
        if (commentEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Не найден комментарий с id = %s".formatted(commentId));
        }
        var commentEntity = commentEntityOptional.get();
        var bookId = commentEntity.getBookId();
        if (isNull(bookId)) {
            throw new EntityNotFoundException("У комментария с id = %s нет ссылки на книгу!".formatted(commentId));
        }
        var bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Комментарий с id = %s ссылается на книгу с id = %s, которой нет"
                    .formatted(commentId, bookId));
        }
        commentEntity.setText(commentText);
        commentEntity = commentRepository.save(commentEntity);
        var book = bookOptional.get();
        var commentFromBookOptional = book.getComments().stream()
                .filter(b -> b.getId().equals(commentId))
                .findFirst();
        commentFromBookOptional.ifPresent(c -> c.setText(commentText));
        bookRepository.save(book);
        return modelMapper.map(commentEntity, CommentDto.class);
    }

    @Override
    @Transactional
    public void deleteById(String commentId) {
        var commentEntityOptional = commentRepository.findById(commentId);
        if (commentEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Не найден комментарий с id = %s".formatted(commentId));
        }
        var commentEntity = commentEntityOptional.get();
        var bookId = commentEntity.getBookId();
        if (isNull(bookId)) {
            throw new EntityNotFoundException("У комментария с id = %s нет ссылки на книгу!".formatted(commentId));
        }
        var bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Комментарий с id = %s ссылается на книгу с id = %s, которой нет"
                    .formatted(commentId, bookId));
        }
        commentRepository.deleteById(commentId);
        var book = bookOptional.get();
        var commentsFromBook = book.getComments().stream()
                .filter(b -> !b.getId().equals(commentId))
                .toList();
        book.setComments(commentsFromBook);
        bookRepository.save(book);
    }
}
