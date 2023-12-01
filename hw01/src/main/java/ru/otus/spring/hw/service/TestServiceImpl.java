package ru.otus.spring.hw.service;

import lombok.AllArgsConstructor;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Question;

import java.util.List;

@AllArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        List<Question> questionList = questionDao.findAll();
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        int questionNumber = 0;
        for (Question question : questionList
        ) {
            questionNumber++;
            ioService.printLine(String.format("Question %d. %s %n", questionNumber, question));
        }
    }
}
