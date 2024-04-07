package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private final Step transformGenreStep;

    private final Step transformAuthorStep;

    private final Step transformBookStep;

    private final Step transformCommentStep;

    @Bean
    public Job importBookLibraryJob(JobRepository jobRepository) {
        return new JobBuilder("importBookLibraryJob", jobRepository)
                .start(splitFlow())
                .next(transformBookStep)
                .next(transformCommentStep)
                .build()        //builds FlowJobBuilder instance
                .build();       //builds Job instance
    }

    @Bean
    public Flow splitFlow() {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flowGenre(), flowAuthor())
                .build();
    }

    @Bean
    public Flow flowGenre() {
        return new FlowBuilder<SimpleFlow>("flowGenre")
                .start(transformGenreStep)
                .build();
    }

    @Bean
    public Flow flowAuthor() {
        return new FlowBuilder<SimpleFlow>("flowAuthor")
                .start(transformAuthorStep)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
