package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookWithCommentsDto {

    private long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    private List<CommentDto> comments = new ArrayList<>();
}
