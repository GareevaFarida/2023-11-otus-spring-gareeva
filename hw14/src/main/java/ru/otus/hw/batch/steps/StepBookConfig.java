package ru.otus.hw.batch.steps;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.batch.CacheExtractor;
import ru.otus.hw.batch.service.CommentService;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import javax.sql.DataSource;
import java.util.List;


@SuppressWarnings("unused")

@Configuration
@RequiredArgsConstructor
public class StepBookConfig {

    private static final String JOB_NAME = "IMPORT_BOOK_JOB";

    private static final String QUERY_FIND_RECORDS = "select id, title, author_id, genre_id from books order by id";

    private final Logger logger = LoggerFactory.getLogger("Batch books");

    private final Logger loggerComments = LoggerFactory.getLogger("Batch comments");

    @StepScope
    @Bean
    JdbcCursorItemReader<BookDto> bookItemReader(DataSource dataSource) {
        JdbcCursorItemReader<BookDto> databaseReader = new JdbcCursorItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_RECORDS);
        databaseReader.setRowMapper((rs, rowNum) -> BookDto.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .authorId(rs.getLong("author_id"))
                .genreId(rs.getLong("genre_id"))
                .build());

        return databaseReader;
    }

    @SneakyThrows
    @StepScope
    @Bean
    ItemWriter<BookDto> bookItemWriter(BookRepository bookRepository,
                                       CommentRepository commentRepository,
                                       CommentService commentService,
                                       CacheExtractor<Genre> genreCache,
                                       CacheExtractor<Author> authorCache) {
        return chunk -> {
            BookDto bookDto = chunk.getItems().get(0);
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(authorCache.getEntityById(bookDto.getAuthorId()));
            book.setGenre(genreCache.getEntityById(bookDto.getGenreId()));
            book = bookRepository.save(book);
            logger.info("Book title='%s', long id = %d, mongo id = '%s'"
                    .formatted(bookDto.getTitle(), bookDto.getId(), book.getId()));
            List<Comment> commentList = commentService.getComments(bookDto.getId(), book.getId());
            if (!commentList.isEmpty()) {
                commentRepository.saveAll(commentList);
                loggerComments.info("Saved %d comments of book (title = '%s')"
                        .formatted(commentList.size(), bookDto.getTitle()));
            }
        };
    }

    @Bean
    public Step transformBookStep(ItemReader<BookDto> reader,
                                  ItemWriter<BookDto> writer,
                                  JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<BookDto, BookDto>chunk(1, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

}
