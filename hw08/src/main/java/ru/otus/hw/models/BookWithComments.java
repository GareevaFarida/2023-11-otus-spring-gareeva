package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "booksWithComments")
public class BookWithComments {
    @Id
    private String id;

    private String bookId;

    private String authorId;

    private String genreId;

    private List<Comment> comments = new ArrayList<>();
}
