package ru.otus.spring.hw02.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw02.config.TestConfig;
import ru.otus.spring.hw02.domain.Student;
import ru.otus.spring.hw02.domain.StudentAnswer;
import ru.otus.spring.hw02.domain.TestResult;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {

    private final IOService ioService;

    private final TestConfig testConfig;

    @Override
    public TestResult createTestResult(Student student, String questionResourcePath) {
        return TestResult.builder()
                .student(student)
                .testingTime(LocalDateTime.now())
                .testSourcePath(questionResourcePath)
                .studentAnswers(new ArrayList<>())
                .build();
    }

    @Override
    public void showResults(@NonNull TestResult testResult) {
        if (isNull(testResult.getStudentAnswers())
                || isEmpty(testResult.getStudentAnswers())) {
            return;
        }

        ioService.printLine("");
        ioService.printLine("Test results: ");
        ioService.printFormattedLine("Student: %s", testResult.getStudent().getFullName());
        ioService.printFormattedLine("Answered questions count: %d", testResult.getStudentAnswers().size());
        ioService.printFormattedLine("Right answers count: %d", testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLine("Congratulations! You passed test!");
            return;
        }
        ioService.printLine("Sorry. You fail test.");

    }

    private void printWrongAnswer(StudentAnswer studentAnswer, int questionNum) {
        if (studentAnswer.isCorrect()) {
            return;
        }
        ioService.printFormattedLine("Answer for question №%d is wrong", questionNum);
    }

}
