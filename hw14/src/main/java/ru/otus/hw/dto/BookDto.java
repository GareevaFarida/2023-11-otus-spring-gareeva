package ru.otus.hw.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDto {

    private long id;

    private String title;

    private long authorId;

    private long genreId;
}
