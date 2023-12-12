package ru.otus.spring.hw02.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppProperties implements TestFileNameProvider {

    @Value("${test.filename:dao/test-for-students.csv}")
    private String testFileName;

    @Override
    public String getTestFileName() {
        return testFileName;
    }
}
