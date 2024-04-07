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
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.batch.cache.CacheExtractor;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

import javax.sql.DataSource;


@SuppressWarnings("unused")

@Configuration
@RequiredArgsConstructor
public class StepAuthorConfig {

    public static final String QUERY_FIND_RECORDS = "select id, full_name from authors order by id";

    private static final String JOB_NAME = "IMPORT_AUTHOR_JOB";

    private final Logger logger = LoggerFactory.getLogger("Batch authors");

    private final CacheExtractor<Author> authorCacheExtractor;

    @StepScope
    @Bean
    ItemProcessor<AuthorDto, Author> authorItemProcessor() {
        return dto -> {
            var author = new Author(ObjectId.get().toString(), dto.getName());
            authorCacheExtractor.put(dto.getId(), author);
            logger.info("Author fullName='{}', long id = {}, mongo id = {}", dto.getName(), dto.getId(), author.getId());
            return author;
        };
    }

    @StepScope
    @Bean
    JdbcCursorItemReader<AuthorDto> authorItemReader(DataSource dataSource) {
        JdbcCursorItemReader<AuthorDto> databaseReader = new JdbcCursorItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_RECORDS);
        databaseReader.setRowMapper((rs, rowNum) ->
                new AuthorDto(rs.getLong("id"), rs.getString("full_name")));
        return databaseReader;
    }

    @StepScope
    @Bean
    MongoItemWriter<Author> authorItemWriter(MongoOperations mongoOperations) {
        MongoItemWriter<Author> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setCollection("authors");
        mongoItemWriter.setMode(MongoItemWriter.Mode.INSERT);
        mongoItemWriter.setTemplate(mongoOperations);
        return mongoItemWriter;
    }

    @Bean
    public Step transformAuthorStep(JdbcCursorItemReader<AuthorDto> reader,
                                    MongoItemWriter<Author> writer,
                                    ItemProcessor<AuthorDto, Author> authorItemProcessor,
                                    JobRepository jobRepository,
                                    PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<AuthorDto, Author>chunk(3, platformTransactionManager)
                .reader(reader)
                .processor(authorItemProcessor)
                .writer(writer)
                .build();
    }

}
