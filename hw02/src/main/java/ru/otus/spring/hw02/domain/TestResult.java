package ru.otus.spring.hw02.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TestResult {

    private final Student student;

    /**
     * Путь к файлу опросника. Предполагаю, что студент будет проходить не единственный тест,
     * и хорошо бы понимать с каким тестом он справился успешно, а с каким нет.
     */
    private final String testSourcePath;

    private final LocalDateTime testingTime;

    private final List<StudentAnswer> studentAnswers;

    private int rightAnswersCount;

    public void addStudentAnswer(StudentAnswer studentAnswer, boolean isAnswerValid) {
        studentAnswers.add(studentAnswer);
        if (isAnswerValid) {
            rightAnswersCount++;
        }
    }
}
