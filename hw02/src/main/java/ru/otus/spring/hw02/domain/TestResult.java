package ru.otus.spring.hw02.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TestResult {
    /**
     * Студент.
     */
    private final Student student;
    /**
     * Путь к опроснику.
     */
    private final String testSourcePath;
    /**
     * Дата время прохождения тестирования.
     */
    private final LocalDateTime testingTime;
    /**
     * Варианты ответов, данные студентом.
     */
    private final List<StudentAnswer> studentAnswers;

    public void addStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswers.add(studentAnswer);
    }
}
