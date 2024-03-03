package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(BookDto book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre())
        );
    }

    public String bookToString(BookWithCommentsDto bookWithComment, BookDto book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s], comments: [%s%s]".formatted(
                bookWithComment.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre()),
                System.lineSeparator(),
                bookWithComment.getComments().stream()
                        .map(commentConverter::commentToString)
                        .collect(Collectors.joining("," + System.lineSeparator())));
    }
}
