package ru.otus.spring.hw03.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw03.config.Formatter;
import ru.otus.spring.hw03.domain.Answer;
import ru.otus.spring.hw03.domain.Question;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.otus.spring.hw03.utils.StringUtils.emptyIfNull;

@RequiredArgsConstructor
@Component
public class QuestionFormatterImpl implements QuestionFormatter {

    private final Formatter appProperties;

    @Override
    public String apply(Question question, String questionPrefix) {
        if (isNull(question) || isNull(question.getText())) {
            return emptyIfNull(questionPrefix);
        }

        if (isEmpty(question.getAnswers())) {
            return emptyIfNull(questionPrefix) + question.getText();
        }

        var answers = question.getAnswers();
        var answersString = IntStream.range(0, question.getAnswers().size())
                .boxed()
                .map(i -> answerToString(answers.get(i), i))
                .collect(Collectors.joining());

        return String.format("%s%n%s%n%s%n",
                emptyIfNull(appProperties.getFormatter().questionDelimiter()),
                questionPrefix + question.getText(),
                answersString);
    }

    private String answerToString(Answer answer, int answerNumber) {
        var answerNumberPrefix = appProperties.getFormatter().answerArabicNumerationEnable()
                ? String.format("%d. ", answerNumber + 1) : "";
        return String.format("%s%s%s%n", emptyIfNull(appProperties.getFormatter().answerTabulation()),
                answerNumberPrefix, answer.getText());
    }
}
