package ru.otus.spring.hw02.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties implements TestFileNameProvider, TestConfig {

    private final String testFileName;

    private final int minAcceptCount;

    public AppProperties(@Value("${test.filename:dao/test-for-students.csv}") String testFileName,
                         @Value("${test.min-accept-count:3}") int minAcceptCount) {
        this.testFileName = testFileName;
        this.minAcceptCount = minAcceptCount;
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return minAcceptCount;
    }
}
