package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookWithComments;
import ru.otus.hw.repositories.BookWithCommentsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookWithCommentsRepository bookWithCommentsRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(String bookId, String commentId) {
        Optional<BookWithComments> bookWithCommentsOptional = bookWithCommentsRepository.findById(bookId);
        if (bookWithCommentsOptional.isEmpty()) {
            return Optional.empty();
        }
        return bookWithCommentsOptional.get()
                .getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findAny()
                .map(val -> modelMapper.map(val, CommentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllCommentsByBookId(String bookId) {
        Optional<BookWithComments> bookWithCommentsOptional = bookWithCommentsRepository.findById(bookId);
        if (bookWithCommentsOptional.isEmpty()) {
            return Collections.emptyList();
        }
        return bookWithCommentsOptional.get()
                .getComments().stream()
                .map(val -> modelMapper.map(val, CommentDto.class))
                .toList();
    }

    @Override
    @Transactional
    public CommentDto update(String bookId, String commentId, String commentText) {
        var bookOptional = bookWithCommentsRepository.getByBookId(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Комментарий с id = %s ссылается на книгу с id = %s, которой нет"
                    .formatted(commentId, bookId));
        }
        var book = bookOptional.get();
        var commentFromBookOptional = book.getComments().stream()
                .filter(b -> b.getId().equals(commentId))
                .findFirst();
        commentFromBookOptional.ifPresent(c -> c.setText(commentText));
        bookWithCommentsRepository.save(book);
        return modelMapper.map(commentFromBookOptional.get(), CommentDto.class);
    }

    @Override
    @Transactional
    public void deleteById(String bookId, String commentId) {
        var bookOptional = bookWithCommentsRepository.getByBookId(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Комментарий с id = %s ссылается на книгу с id = %s, которой нет"
                    .formatted(commentId, bookId));
        }
        var book = bookOptional.get();
        var commentsFromBook = book.getComments().stream()
                .filter(b -> !b.getId().equals(commentId))
                .toList();
        book.setComments(commentsFromBook);
        bookWithCommentsRepository.save(book);
    }
}
