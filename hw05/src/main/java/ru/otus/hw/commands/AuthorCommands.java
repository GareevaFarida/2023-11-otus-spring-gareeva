package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by id", key = "abid")
    public String findAuthorById(long id) {
        return authorService.findById(id)
                .map(authorConverter::authorToString)
                .orElse("Sorry, no one author with id = %d was found".formatted(id));
    }

    @ShellMethod(value = "Delete author by id", key = "adel")
    public String deleteAuthorById(long id) {
        try {
            authorService.deleteById(id);
            return "Deleted author with id=%d".formatted(id);
        } catch (Exception e) {
            return "Author has not been deleted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Insert new author", key = "ains")
    public String insertAuthor(String name) {
        try {
            var author = authorService.insert(name);
            return "Successfully inserted author with %s".formatted(authorConverter.authorToString(author));
        } catch (Exception e) {
            return "Author has not been inserted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Update existing author", key = "aupd")
    public String updateAuthor(long id, String name) {
        if (id == 0L) {
            return "No one author with id=%d was found".formatted(id);
        }
        try {
            var author = authorService.update(id, name);
            return "Successfully updated author with %s".formatted(authorConverter.authorToString(author));
        } catch (Exception e) {
            return "Author has not been updated due to an error: %s".formatted(e.getMessage());
        }
    }
}
