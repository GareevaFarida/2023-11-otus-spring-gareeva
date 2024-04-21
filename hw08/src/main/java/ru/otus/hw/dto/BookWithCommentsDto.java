package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.hw.models.Comment;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookWithCommentsDto {

    private String id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    private List<Comment> comments = new ArrayList<>();
}
