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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")

@Configuration
@RequiredArgsConstructor
public class StepAuthorConfig implements CacheExtractor<Author> {

    private static final String JOB_NAME = "IMPORT_AUTHOR_JOB";

    private static final String QUERY_FIND_RECORDS = "select id, full_name from authors order by id";

    private final Logger logger = LoggerFactory.getLogger("Batch authors");

    private Map<Long, Author> cache = new HashMap<>();

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

    @SneakyThrows
    @StepScope
    @Bean
    ItemWriter<AuthorDto> authorItemWriter(AuthorRepository authorRepository) {
        return chunk -> {
            AuthorDto authorDto = chunk.getItems().get(0);
            Author author = authorRepository.save(new Author(null, authorDto.getName()));
            cache.put(authorDto.getId(), author);
            logger.info("Author name='%s', long id = %d, mongo id = '%s'"
                    .formatted(authorDto.getName(), authorDto.getId(), author.getId()));
        };
    }

    @Bean
    public Step transformAuthorStep(ItemReader<AuthorDto> reader,
                                    ItemWriter<AuthorDto> writer,
                                    JobRepository jobRepository,
                                    PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<AuthorDto, AuthorDto>chunk(1, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Override
    public Author getEntityById(long id) {
        if (!cache.containsKey(id)) {
            throw new IllegalArgumentException("Not found Author with id = %d".formatted(id));
        }
        return cache.get(id);
    }
}
