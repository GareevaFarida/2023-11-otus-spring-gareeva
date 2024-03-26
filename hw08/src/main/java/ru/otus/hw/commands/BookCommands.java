package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ba")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        var bookDto = bookService.findById(id);
        var bookWithCommentDto = bookService.findByIdWithComments(id);
        if (bookDto.isEmpty() || bookWithCommentDto.isEmpty()) {
            return "Book with id %s not found".formatted(id);
        }
        return bookConverter.bookToString(bookWithCommentDto.get(), bookDto.get());
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        try {
            var savedBook = bookService.insert(title, authorId, genreId);
            return "Successfully inserted book with %s".formatted(bookConverter.bookToString(savedBook));
        } catch (Exception e) {
            return "Book has not been inserted due to an error: %s".formatted(e.getMessage());
        }
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        if (isNull(id)) {
            return "No one book with id=%s was found".formatted(id);
        }
        try {
            var savedBook = bookService.update(id, title, authorId, genreId);
            return "Successfully updated book with %s".formatted(bookConverter.bookToString(savedBook));
        } catch (Exception e) {
            return "Book has not been updated due to an error: %s".formatted(e.getMessage());
        }
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public String deleteBook(String id) {
        try {
            bookService.deleteById(id);
            return "Deleted book with id=%s".formatted(id);
        } catch (Exception e) {
            return "Book has not been deleted due to an error: %s".formatted(e.getMessage());
        }
    }
}
