package ru.otus.spring.hw02.service;

import ru.otus.spring.hw02.domain.Student;
import ru.otus.spring.hw02.domain.TestResult;

public interface ResultService {
    TestResult createTestResult(Student student, String questionResourcePath);

    void showResults(TestResult testResult);
}
