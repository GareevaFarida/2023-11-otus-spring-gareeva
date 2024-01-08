package ru.otus.spring.hw04.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw04.config.FormatterPropsProvider;
import ru.otus.spring.hw04.domain.Answer;
import ru.otus.spring.hw04.domain.Question;
import ru.otus.spring.hw04.utils.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
public class QuestionFormatterImpl implements QuestionFormatter {

    private final FormatterPropsProvider formatterProperties;

    @Override
    public String apply(Question question, String questionPrefix) {
        if (isNull(question) || isNull(question.getText())) {
            return StringUtils.emptyIfNull(questionPrefix);
        }

        if (isEmpty(question.getAnswers())) {
            return StringUtils.emptyIfNull(questionPrefix) + question.getText();
        }

        var answers = question.getAnswers();
        var answersString = IntStream.range(0, question.getAnswers().size())
                .boxed()
                .map(i -> answerToString(answers.get(i), i))
                .collect(Collectors.joining());

        return String.format("%s%n%s%n%s%n",
                StringUtils.emptyIfNull(formatterProperties.getFormatterProperties().questionDelimiter()),
                questionPrefix + question.getText(),
                answersString);
    }

    private String answerToString(Answer answer, int answerNumber) {
        var answerNumberPrefix = formatterProperties.getFormatterProperties().answerArabicNumerationEnable()
                ? String.format("%d. ", answerNumber + 1) : "";
        return String.format("%s%s%s%n", StringUtils.emptyIfNull(
                formatterProperties.getFormatterProperties().answerTabulation()),
                answerNumberPrefix, answer.getText());
    }
}
