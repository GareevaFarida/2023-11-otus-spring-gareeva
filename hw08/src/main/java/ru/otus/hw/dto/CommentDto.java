package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentDto {

    private String id;

    private String text;

    private BookDto book;

    @Override
    public String toString() {
        return "Comment{id=%s, text='%s'}".formatted(id, text);
    }
}