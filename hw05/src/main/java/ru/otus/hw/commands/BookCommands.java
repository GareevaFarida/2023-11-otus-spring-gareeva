package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

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
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, long genreId) {
        try {
            var savedBook = bookService.insert(title, authorId, genreId);
            return "Successfully inserted book with %s".formatted(bookConverter.bookToString(savedBook));
        } catch (Exception e) {
            return "Book has not been inserted due to an error: %s".formatted(e.getMessage());
        }
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, long genreId) {
        if (id == 0L) {
            return "No one book with id=%d was found".formatted(id);
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
    public String deleteBook(long id) {
        try {
            bookService.deleteById(id);
            return "Deleted book with id=%d".formatted(id);
        } catch (Exception e) {
            return "Book has not been deleted due to an error: %s".formatted(e.getMessage());
        }
    }
}
