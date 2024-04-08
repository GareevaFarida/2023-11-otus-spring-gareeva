package ru.otus.hw.repositories;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Comment;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

@Service
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Comment> findById(String bookId, String commentId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("id").is(bookId))
                , unwind("comments")
                , match(Criteria.where("comments.id").is(commentId))
                , project().andExclude("_id").and("comments._id").as("_id").and("comments.text").as("text")
        );

        List<Comment> res = mongoTemplate.aggregate(aggregation, Book.class, Comment.class).getMappedResults();

        if (res.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(res.get(0));
    }

    @Override
    public List<Comment> findAllCommentsByBookId(String bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            return Collections.emptyList();
        }
        return bookOptional.get()
                .getComments().stream()
                .map(val -> modelMapper.map(val, Comment.class))
                .toList();
    }

    @Override
    public Comment insert(String bookId, String commentText) {
        var comment = new Comment(commentText);
        Update update = new Update().push("comments", comment);
        Query query = new Query(Criteria.where("_id").is(bookId));
        mongoTemplate.updateMulti(query, update, Book.class);
        return comment;
    }

    @Override
    public Comment update(String bookId, String commentId, String text) {
        var update = new Update()
                .set("comments.$[element]", new Comment(commentId, text))
                .filterArray(Criteria.where("element._id").is(new ObjectId(commentId)));

        var query = new Query(Criteria.where("_id").is(bookId));

        mongoTemplate.updateMulti(query, update, Book.class);

        var commentOptional = findById(bookId, commentId);
        if (commentOptional.isPresent()) {
            return commentOptional.get();
        }
        throw new EntityNotFoundException("Не найден комментарий с id = %s к книге с id = %s"
                .formatted(commentId, bookId));
    }

    @Override
    public void deleteById(String bookId, String commentId) {
        Query query = new Query(Criteria.where("id").is(bookId));
        query.fields().elemMatch("comments", Criteria.where("id").is(commentId));
        Update update = new Update().pull("comments", new BasicDBObject("id", commentId));
        mongoTemplate.updateMulti(query, update, Book.class);
    }

}
