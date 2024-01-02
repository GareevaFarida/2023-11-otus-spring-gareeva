package ru.otus.spring.hw03.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw03.dao.QuestionDao;
import ru.otus.spring.hw03.domain.Answer;
import ru.otus.spring.hw03.domain.Question;
import ru.otus.spring.hw03.domain.Student;
import ru.otus.spring.hw03.domain.StudentAnswer;
import ru.otus.spring.hw03.domain.TestResult;
import ru.otus.spring.hw03.utils.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

@Slf4j
@Service

public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionFormatter formatter;

    public TestServiceImpl(LocalizedIOService ioService,
                           QuestionDao questionDao,
                           QuestionFormatter formatter) {
        this.ioService = ioService;
        this.questionDao = questionDao;
        this.formatter = formatter;
    }

    @Override
    public TestResult executeTestFor(Student student) {
        var testResult = new TestResult(student);
        var questionList = questionDao.findAll();
        ioService.printLine(StringUtils.EMPTY);
        ioService.printFormattedLineLocalized("TestService.answer.the.questions");
        for (int num = 0; num < questionList.size(); num++) {
            var question = questionList.get(num);
            var studentAnswer = askQuestion(question, num);
            if (nonNull(studentAnswer)) {
                var isAnswerValid = isAnswerValid(studentAnswer);
                testResult.addStudentAnswer(studentAnswer, isAnswerValid);
            }
        }
        return testResult;
    }

    public StudentAnswer askQuestion(Question question, int questionNumber) {
        var questionLine = getQuestionLine(question, questionNumber);
        ioService.printLine(questionLine);
        String promptDelimiter = ioService.getMessage("TestService.answers.delimiter");
        String promptError = ioService.getMessage("TestService.answers.error", 1, question.getAnswers().size());
        try {
            Set<Integer> answerList = ioService.readIntegerSetInRangeWithDelimiterAndPrompt(1,
                    question.getAnswers().size(), " ", promptDelimiter, promptError);
            return new StudentAnswer(question, answerList);
        } catch (IllegalArgumentException ex) {
            ioService.printError(ex.getMessage());
        }
        return null;
    }

    private boolean isAnswerValid(StudentAnswer studentAnswer) {
        List<Answer> answers = studentAnswer.getQuestion().getAnswers();
        var resultList = IntStream.range(0, answers.size())
                .boxed()
                .map(i -> answers.get(i).isCorrect() == studentAnswer.getCheckedAnswers().contains(i + 1))
                .distinct()
                .collect(Collectors.toList());
        return resultList.size() == 1
                && Boolean.TRUE.equals(resultList.get(0));
    }

    private String getQuestionLine(Question question, int num) {
        var questionPrefix = ioService.getMessage("TestService.question.number", num + 1);
        return formatter.apply(question, questionPrefix);
    }
}
