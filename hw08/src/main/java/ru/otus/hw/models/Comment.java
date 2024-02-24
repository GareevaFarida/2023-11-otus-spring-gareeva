package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String text;

    private Book book;

    @Override
    public String toString() {
        return "Comment{id=%s, text='%s'}".formatted(id, text);
    }
}