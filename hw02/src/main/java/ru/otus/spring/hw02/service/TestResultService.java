package ru.otus.spring.hw02.service;

import ru.otus.spring.hw02.domain.Question;
import ru.otus.spring.hw02.domain.TestResult;
import ru.otus.spring.hw02.exceptions.AnswerReadException;

public interface TestResultService {
    TestResult createTestResult();

    void addAnswer(TestResult testResult, Question question) throws AnswerReadException;

    void checkResults(TestResult testResult);
}
