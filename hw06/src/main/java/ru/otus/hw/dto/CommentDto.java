package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {

    private long id;

    private String text;

    private Book book;

    @Override
    public String toString() {
        //это борьба со stackOverFlowException
        return "Comment{id=%d, text='%s', book={id=%d, title='%s', genre={%s}, author={%s}}}"
                .formatted(id,
                        text,
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getAuthor());
    }
}