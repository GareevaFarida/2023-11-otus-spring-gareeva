package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.hw.models.Book;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CommentDto {

    @Getter
    private long id;

    @Getter
    private String text;

    private Book book;

    @Override
    public String toString() {
        return "Comment{id=%d, text='%s'}".formatted(id, text);
    }
}