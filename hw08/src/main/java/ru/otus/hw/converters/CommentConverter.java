package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.Comment;

@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        return "Id: %s, text: '%s'".formatted(comment.getId(), comment.getText());
    }
}
