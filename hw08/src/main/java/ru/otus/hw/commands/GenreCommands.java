package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ga")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));

    }

    @ShellMethod(value = "Find genre by id", key = "gbid")
    public String findGengeById(String id) {
        return genreService.findById(id)
                .map(genreConverter::genreToString)
                .orElse("Sorry, no one genre with id = %s was found".formatted(id));
    }

    @ShellMethod(value = "Delete genre by id", key = "gdel")
    public String deleteGenreById(String id) {
        try {
            genreService.deleteById(id);
            return "Deleted genre with id=%s".formatted(id);
        } catch (Exception e) {
            return "Genre has not been deleted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Insert new genre", key = "gins")
    public String insertGenre(String name) {
        try {
            var genre = genreService.insert(name);
            return "Successfully inserted genre with %s".formatted(genreConverter.genreToString(genre));
        } catch (Exception e) {
            return "Genre has not been inserted due to an error: %s".formatted(e.getMessage());
        }
    }

    @ShellMethod(value = "Update existing genre", key = "gupd")
    public String updateGenre(String id, String name) {
        if (isNull(id)) {
            return "No one genre with id=%s was found".formatted(id);
        }
        try {
            var genre = genreService.update(id, name);
            return "Successfully updated genre with %s".formatted(genreConverter.genreToString(genre));
        } catch (Exception e) {
            return "Genre has not been updated due to an error: %s".formatted(e.getMessage());
        }
    }
}
