package ru.otus.hw.batch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Transactional(readOnly = true)
    public List<Comment> getComments(long bookId, String mongoId) {
        return jdbcOperations.query("select comment_text from comments where book_id=:id",
                new MapSqlParameterSource("id", bookId),
                (rs, rowNum) -> new Comment(null, mongoId, rs.getString("comment_text"))
        );
    }
}
