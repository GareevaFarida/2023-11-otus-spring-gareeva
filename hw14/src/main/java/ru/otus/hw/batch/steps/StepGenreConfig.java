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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

import javax.sql.DataSource;


@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class StepGenreConfig {

    public static final String QUERY_FIND_RECORDS = "select id, name from genres order by id";

    private static final String JOB_NAME = "IMPORT GENRE JOB";

    private final Logger logger = LoggerFactory.getLogger("Batch genres");

    private final CacheExtractor<Genre> genreCacheExtractor;

    @StepScope
    @Bean
    JdbcCursorItemReader<GenreDto> genreItemReader(DataSource dataSource) {
        JdbcCursorItemReader<GenreDto> databaseReader = new JdbcCursorItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_RECORDS);
        databaseReader.setRowMapper((rs, rowNum) -> new GenreDto(rs.getLong("id"), rs.getString("name")));
        return databaseReader;
    }

    @StepScope
    @Bean
    MongoItemWriter<Genre> genreItemWriter(MongoOperations mongoOperations) {
        var mongoItemWrater = new MongoItemWriter<Genre>();
        mongoItemWrater.setTemplate(mongoOperations);
        mongoItemWrater.setMode(MongoItemWriter.Mode.INSERT);
        mongoItemWrater.setCollection("genres");
        return mongoItemWrater;
    }

    @StepScope
    @Bean
    ItemProcessor<GenreDto, Genre> genreItemProcessor() {
        return genreDto -> {
            var genre = new Genre(ObjectId.get().toString(), genreDto.getName());
            genreCacheExtractor.put(genreDto.getId(), genre);
            logger.info("Genre name='{}', long id = {}, mongo id = '{}'",
                    genreDto.getName(), genreDto.getId(), genre.getId());
            return genre;
        };
    }

    @Bean
    public Step transformGenreStep(ItemReader<GenreDto> reader,
                                   ItemWriter<Genre> writer,
                                   ItemProcessor<GenreDto, Genre> processor,
                                   JobRepository jobRepository,
                                   PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("transformGenreStep", jobRepository)
                .<GenreDto, Genre>chunk(3, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
