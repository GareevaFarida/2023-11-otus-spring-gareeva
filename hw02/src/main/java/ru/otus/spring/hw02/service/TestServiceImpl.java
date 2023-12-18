package ru.otus.spring.hw02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw02.config.TestFileNameProvider;
import ru.otus.spring.hw02.dao.QuestionDao;
import ru.otus.spring.hw02.domain.Question;
import ru.otus.spring.hw02.domain.Student;
import ru.otus.spring.hw02.domain.StudentAnswer;
import ru.otus.spring.hw02.domain.TestResult;
import ru.otus.spring.hw02.utils.StringUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionFormatter formatter;

    private final ResultService resultService;

    private final TestFileNameProvider testFileNameProvider;

    @Override
    public TestResult executeTestFor(Student student) {
        String questionResource = testFileNameProvider.getTestFileName();
        var testResult = resultService.createTestResult(student, questionResource);
        var questionList = questionDao.findAll();
        ioService.printLine(StringUtils.EMPTY);
        ioService.printFormattedLine("Please answer the questions below%n");
        for (int num = 0; num < questionList.size(); num++) {
            var question = questionList.get(num);
            var questionLine = getQuestionLine(question, num);
            ioService.printLine(questionLine);
            addAnswer(testResult, question);
        }
        return testResult;
    }

    public void addAnswer(TestResult testResult, Question question) {
        String prompt = "Enter number/numbers of correct answers with space delimiter";
        try {
            Set<Integer> answerList = ioService.readIntegerSetInRangeWithDelimiterAndPrompt(1,
                    question.getAnswers().size(), " ", prompt);
            var studentAnswer = new StudentAnswer(question, answerList);
            testResult.addStudentAnswer(studentAnswer);
        } catch (IllegalArgumentException ex) {
            ioService.printError(ex.getMessage());
        }
    }

    private String getQuestionLine(Question question, int num) {
        var questionPrefix = String.format("Question %d. ", num + 1);
        return formatter.apply(question, questionPrefix);
    }
}
