package ru.otus.hw.projections;

import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Projection(name = "withoutComments", types = {Book.class})
public interface BookProjection {
    String getTitle();

    Author getAuthor();

    Genre getGenre();
}
