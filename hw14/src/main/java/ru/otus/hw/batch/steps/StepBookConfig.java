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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import javax.sql.DataSource;


@SuppressWarnings("unused")

@Configuration
@RequiredArgsConstructor
public class StepBookConfig {

    public static final String QUERY_FIND_RECORDS = "select id, title, author_id, genre_id from books order by id";

    private static final String JOB_NAME = "IMPORT_BOOK_JOB";

    private final Logger logger = LoggerFactory.getLogger("Batch books");

    private final CacheExtractor<String> bookCacheExtractor;

    private final CacheExtractor<Genre> genreCacheExtractor;

    private final CacheExtractor<Author> authorCacheExtractor;

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

    @StepScope
    @Bean
    ItemProcessor<BookDto, Book> bookItemProcessor() {
        return bookDto -> {
            var book = new Book();
            book.setId(ObjectId.get().toString());
            book.setTitle(bookDto.getTitle());
            var author = authorCacheExtractor.getEntityById(bookDto.getAuthorId());
            book.setAuthor(author);
            var genre = genreCacheExtractor.getEntityById(bookDto.getGenreId());
            book.setGenre(genre);
            bookCacheExtractor.put(bookDto.getId(), book.getId());
            logger.info("Book title='{}', long id = {}, mongo id = {}",
                    bookDto.getTitle(), bookDto.getId(), book.getId());
            return book;
        };
    }

    @StepScope
    @Bean
    MongoItemWriter<Book> bookItemWriter(MongoOperations mongoOperations) {
        var writer = new MongoItemWriter<Book>();
        writer.setCollection("books");
        writer.setMode(MongoItemWriter.Mode.INSERT);
        writer.setTemplate(mongoOperations);
        return writer;
    }

    @Bean
    public Step transformBookStep(ItemReader<BookDto> reader,
                                  ItemProcessor<BookDto, Book> bookItemProcessor,
                                  ItemWriter<Book> writer,
                                  JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<BookDto, Book>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(bookItemProcessor)
                .writer(writer)
                .build();
    }

}
