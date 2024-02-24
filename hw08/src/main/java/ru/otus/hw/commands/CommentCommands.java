package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.CommentService;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all comments by bookId", key = "ca")
    public String findAllCommentsByBookId(String bookId) {
        return commentService.findAllCommentsByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));

    }

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Sorry, no one comment with id = %s was found".formatted(id));
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public String deleteCommentById(String id) {
        try {
            commentService.deleteById(id);
            return "Deleted comment with id=%s".formatted(id);
        } catch (Exception e) {
            return "Comment has not been deleted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Insert new comment to book with id", key = "cins")
    public String insertComment(String bookId, String commentText) {
        BookDto book = new BookDto();
        book.setId(bookId);
        CommentDto commentDto = new CommentDto(null, commentText, book);
        try {
            var savedComment = commentService.save(commentDto);
            return "Successfully inserted comment with %s".formatted(commentConverter.commentToString(savedComment));
        } catch (Exception e) {
            return "comment has not been inserted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Update existing comment with id", key = "cupd")
    public String updateCommentById(String id, String commentText) {
        if (isNull(id)) {
            return "No one comment with id=%s was found".formatted(id);
        }
        Optional<CommentDto> commentOptional = commentService.findById(id);
        try {
            if (commentOptional.isPresent()) {
                CommentDto commentDto = commentOptional.get();
                commentDto.setText(commentText);
                commentService.save(commentDto);
                return "Successfully updated comment with %s".formatted(id);
            } else {
                throw new Exception("Comment with id=%s does not exist".formatted(id));
            }
        } catch (Exception e) {
            return "Comment has not been updated due to an error: %s".formatted(e.getMessage());
        }
    }
}
