package ru.otus.spring.hw04.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TestResult {

    private final Student student;

    private final LocalDateTime testingTime;

    private final List<StudentAnswer> studentAnswers;

    private int rightAnswersCount;

    public TestResult(Student student) {
        this.student = student;
        this.testingTime = LocalDateTime.now();
        this.studentAnswers = new ArrayList<>();
    }

    public void addStudentAnswer(StudentAnswer studentAnswer, boolean isAnswerValid) {
        studentAnswers.add(studentAnswer);
        if (isAnswerValid) {
            rightAnswersCount++;
        }
    }
}
