package ru.otus.spring.hw02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw02.dao.QuestionDao;
import ru.otus.spring.hw02.exceptions.AnswerReadException;
import ru.otus.spring.hw02.utils.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionFormatter formatter;

    private final TestResultService testResultService;

    @Override
    public void executeTest() {
        var testResult = testResultService.createTestResult();
        var questionList = questionDao.findAll();
        ioService.printLine(StringUtils.EMPTY);
        ioService.printFormattedLine("Please answer the questions below%n");
        for (int num = 0; num < questionList.size(); num++) {
            var question = questionList.get(num);
            var questionPrefix = String.format("Question %d. ", num + 1);
            var questionLine = formatter.apply(question, questionPrefix);
            ioService.printLine(questionLine);
            ioService.printLine("Enter number/numbers of correct answers with space delimiter");
            try {
                testResultService.addAnswer(testResult, question);
            } catch (AnswerReadException e) {
                ioService.printError(e.getMessage());
                num--;
            }
        }
        testResultService.checkResults(testResult);
    }
}
