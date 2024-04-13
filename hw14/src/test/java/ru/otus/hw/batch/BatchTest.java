package ru.otus.hw.batch;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jdbc.core.JdbcOperations;
import ru.otus.hw.batch.steps.StepAuthorConfig;
import ru.otus.hw.batch.steps.StepBookConfig;
import ru.otus.hw.batch.steps.StepCommentConfig;
import ru.otus.hw.batch.steps.StepGenreConfig;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class BatchTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private JdbcOperations jdbcOperations;

    private static final String JOB_NAME = "importBookLibraryJob";

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
        mongoOperations.dropCollection(Author.class);
        mongoOperations.dropCollection(Genre.class);
        mongoOperations.dropCollection(Book.class);
        mongoOperations.dropCollection(Comment.class);
    }

    @DisplayName("Проверяет количество мигрированных сущностей")
    @SneakyThrows
    @Test
    public void testJob() {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        var booksList = jdbcOperations.queryForList(StepBookConfig.QUERY_FIND_RECORDS);
        var books = mongoOperations.findAll(Book.class);
        assertThat(books.size()).isEqualTo(booksList.size());

        var authorsList = jdbcOperations.queryForList(StepAuthorConfig.QUERY_FIND_RECORDS);
        var authors = mongoOperations.findAll(Author.class);
        assertThat(authors.size()).isEqualTo(authorsList.size());

        var genresList = jdbcOperations.queryForList(StepGenreConfig.QUERY_FIND_RECORDS);
        var genres = mongoOperations.findAll(Genre.class);
        assertThat(genres.size()).isEqualTo(genresList.size());

        var commentsList = jdbcOperations.queryForList(StepCommentConfig.QUERY_FIND_RECORDS);
        var comments = mongoOperations.findAll(Comment.class);
        assertThat(comments.size()).isEqualTo(commentsList.size());
    }

}
