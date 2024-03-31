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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")

@Configuration
@RequiredArgsConstructor
public class StepGenreConfig implements CacheExtractor<Genre> {

    private static final String QUERY_FIND_RECORDS = "select id, name from genres order by id";

    private static final String JOB_NAME = "IMPORT GENRE JOB";

    private final Logger logger = LoggerFactory.getLogger("Batch genres");

    private Map<Long, Genre> cache = new HashMap<>();

    @StepScope
    @Bean
    JdbcCursorItemReader<GenreDto> genreItemReader(DataSource dataSource) {
        JdbcCursorItemReader<GenreDto> databaseReader = new JdbcCursorItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_RECORDS);
        databaseReader.setRowMapper((rs, rowNum) -> new GenreDto(rs.getLong("id"), rs.getString("name")));
        return databaseReader;
    }

    @SneakyThrows
    @StepScope
    @Bean
    ItemWriter<GenreDto> genreItemWriter(GenreRepository genreRepository) {
        return chunk -> {
            GenreDto genreDto = chunk.getItems().get(0);
            Genre genre = genreRepository.save(new Genre(null, genreDto.getName()));
            cache.put(genreDto.getId(), genre);
            logger.info("Genre name='%s', long id = %d, mongo id = '%s'"
                    .formatted(genreDto.getName(), genreDto.getId(), genre.getId()));
        };
    }

    @Bean
    public Step transformGenreStep(ItemReader<GenreDto> reader,
                                   ItemWriter<GenreDto> writer,
                                   JobRepository jobRepository,
                                   PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformGenreStep", jobRepository)
                .<GenreDto, GenreDto>chunk(1, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Override
    public Genre getEntityById(long id) {
        if (!cache.containsKey(id)) {
            throw new IllegalArgumentException("Not found Genre with id = %d".formatted(id));
        }
        return cache.get(id);
    }
}
