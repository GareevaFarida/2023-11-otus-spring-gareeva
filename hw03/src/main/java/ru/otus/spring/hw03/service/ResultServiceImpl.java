package ru.otus.spring.hw03.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw03.config.TestConfig;
import ru.otus.spring.hw03.domain.TestResult;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class ResultServiceImpl implements ResultService {

    private final LocalizedIOService ioService;

    private final TestConfig testConfig;

    public ResultServiceImpl(@Qualifier("localizedIOServiceImpl") LocalizedIOService ioService, TestConfig testConfig) {
        this.ioService = ioService;
        this.testConfig = testConfig;
    }

    @Override
    public void showResults(@NonNull TestResult testResult) {
        if (isNull(testResult.getStudentAnswers())
                || isEmpty(testResult.getStudentAnswers())) {
            return;
        }

        ioService.printLine("");
        ioService.printLineLocalized("ResultService.test.results");
        ioService.printFormattedLineLocalized("ResultService.student", testResult.getStudent().getFullName());
        ioService.printFormattedLineLocalized("ResultService.answered.questions.count", testResult.getStudentAnswers().size());
        ioService.printFormattedLineLocalized("ResultService.right.answers.count", testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLineLocalized("ResultService.passed.test");
            return;
        }
        ioService.printLineLocalized("ResultService.fail.test");

    }

}
