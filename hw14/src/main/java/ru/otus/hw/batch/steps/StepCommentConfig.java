package ru.otus.hw.batch.steps;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.batch.cache.CacheExtractor;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import javax.sql.DataSource;


@SuppressWarnings("unused")

@Configuration
@RequiredArgsConstructor
public class StepCommentConfig {

    public static final String QUERY_FIND_RECORDS = "select id, book_id, comment_text from comments order by id";

    private static final String JOB_NAME = "IMPORT_COMMENT_JOB";

    private final Logger logger = LoggerFactory.getLogger("Batch comments");

    private final CacheExtractor<String> bookCacheExtractor;

    @StepScope
    @Bean
    JdbcCursorItemReader<CommentDto> commentItemReader(DataSource dataSource) {
        JdbcCursorItemReader<CommentDto> databaseReader = new JdbcCursorItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_RECORDS);
        databaseReader.setRowMapper((rs, rowNum) ->
                new CommentDto(rs.getLong("id"),
                        rs.getString("comment_text"), rs.getLong("book_id")));
        return databaseReader;
    }

    @StepScope
    @Bean
    ItemProcessor<CommentDto, Comment> commentItemProcessor() {
        return commentDto -> {
            var bookId = bookCacheExtractor.getEntityById(commentDto.getBookId());
            var comment = new Comment(ObjectId.get().toString(), bookId, commentDto.getCommentText());
            logger.info("Comment long id = {}, mongo id = {}", commentDto.getId(), comment.getId());
            return comment;
        };
    }

    @StepScope
    @Bean
    MongoItemWriter<Comment> commentItemWriter(MongoOperations mongoOperations) {
        var writer = new MongoItemWriter<Comment>();
        writer.setCollection("comments");
        writer.setMode(MongoItemWriter.Mode.INSERT);
        writer.setTemplate(mongoOperations);
        return writer;
    }

    @Bean
    public Step transformCommentStep(ItemReader<CommentDto> reader,
                                     ItemProcessor<CommentDto, Comment> bookItemProcessor,
                                     ItemWriter<Comment> writer,
                                     JobRepository jobRepository,
                                     PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformCommentStep", jobRepository)
                .<CommentDto, Comment>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(bookItemProcessor)
                .writer(writer)
                .build();
    }

}
