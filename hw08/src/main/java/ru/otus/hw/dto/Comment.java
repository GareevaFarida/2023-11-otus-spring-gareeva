package ru.otus.hw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@NoArgsConstructor
@Getter
@Setter
/**
 * Это не доменная сущность, это просто dto, список которых
 * хранится в документе "books", поэтому тут нет ид книги.
 * Тут есть только id комментария, по которому его можно
 * определить в списке комментариев конкретной книги
 * для возможности удаления.
 */
public class Comment {

    private String id;

    private String text;

    public Comment(String text) {
        this.id = ObjectId.get().toString();
        this.text = text;
    }
}