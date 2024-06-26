package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.repositories.CommentRepository;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all comments by bookId", key = "ca")
    public String findAllCommentsByBookId(String bookId) {
        return commentRepository.findAllCommentsByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));

    }

    @ShellMethod(value = "Find comment by book_id and comment_id", key = "cbid")
    public String findCommentById(String bookId, String commentId) {
        return commentRepository.findById(bookId, commentId)
                .map(commentConverter::commentToString)
                .orElse("Sorry, no one comment with id = %s of book with id = %s was found"
                        .formatted(commentId, bookId));
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public String deleteCommentById(String bookId, String commentId) {
        try {
            commentRepository.deleteById(bookId, commentId);
            return "Deleted comment with id=%s".formatted(commentId);
        } catch (Exception e) {
            return "Comment has not been deleted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Update comment by bookId and commentId", key = "cupd")
    public String updateCommentByBookIdAndCommentId(String bookId, String commentId, String text) {
        try {
            commentRepository.update(bookId, commentId, text);
            return "Updated comment with id=%s".formatted(commentId);
        } catch (Exception e) {
            return "Comment has not been updated due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Insert new comment to book with id", key = "cins")
    public String insertComment(String bookId, String commentText) {
        try {
            var commentId = commentRepository.insert(bookId, commentText);
            return "Successfully inserted comment with id=%s".formatted(commentId);
        } catch (Exception e) {
            return "comment has not been inserted due to an error: %s".formatted(e.getMessage());
        }
    }

}
