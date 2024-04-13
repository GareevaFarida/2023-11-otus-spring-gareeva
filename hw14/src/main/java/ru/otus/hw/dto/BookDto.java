package ru.otus.hw.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDto {

    private final long id;

    private final String title;

    private final long authorId;

    private final long genreId;
}
