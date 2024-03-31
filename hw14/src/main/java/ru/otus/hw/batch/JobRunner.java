package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRunner implements CommandLineRunner {

    private final Job jobGenreAuthor;

    private final JobLauncher jobLauncher;

    @Override
    public void run(String... args) {
        launch();
    }

    @SneakyThrows
    private void launch() {
        jobLauncher.run(jobGenreAuthor, new JobParametersBuilder().toJobParameters());
    }

}
