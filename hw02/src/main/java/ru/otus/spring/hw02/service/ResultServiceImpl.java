package ru.otus.spring.hw02.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw02.config.TestConfig;
import ru.otus.spring.hw02.domain.TestResult;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {

    private final IOService ioService;

    private final TestConfig testConfig;

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
        ioService.printLine("Sorry. You failed test.");

    }

}
