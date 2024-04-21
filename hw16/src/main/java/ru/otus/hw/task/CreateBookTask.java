package ru.otus.hw.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateBookTask {
    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    private final BookConverter bookConverter;

    private final TaskSettings taskSettings;

    @Scheduled(fixedDelay = 7, timeUnit = TimeUnit.SECONDS)
    public void createRandomBooks() {
        if (!taskSettings.isBookTaskEnable()) {
            return;
        }
        String postfix = String.valueOf(System.currentTimeMillis());
        AuthorDto author = authorService.insert("author_" + postfix);
        GenreDto genre = genreService.insert("genre_" + postfix);
        BookDto book = bookService.insert("book_" + postfix, author.getId(), genre.getId());
        log.info("Опубликована новая книга {}", bookConverter.bookToString(book));
    }
}
