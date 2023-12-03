package ru.otus.spring.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.utils.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionFormatter formatter;

    @Override
    public void executeTest() {
        List<Question> questionList = questionDao.findAll();
        ioService.printLine(StringUtils.EMPTY);
        ioService.printFormattedLine("Please answer the questions below%n");
        int questionNumber = 0;
        for (Question question : questionList
        ) {
            ioService.printLine(
                    formatter.apply(question,
                            String.format("Question %d. ", ++questionNumber)
                    )
            );
        }
    }
}
