package ru.otus.spring.hw02.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw02.domain.Answer;
import ru.otus.spring.hw02.domain.Question;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.otus.spring.hw02.utils.StringUtils.emptyIfNull;

@Component
public class QuestionFormatterImpl implements QuestionFormatter {

    private final String answerTabulation;

    private final boolean answerArabicNumerationEnable;

    private final String questionDelimiter;

    public QuestionFormatterImpl(@Value("${test.formatter.tabulation:    }") String answerTabulation,
                                 @Value("${test.formatter.answer-arabic-numeration-enable:true}")
                                         boolean answerArabicNumerationEnable,
                                 @Value("${test.formatter.question-delimiter:}") String questionDelimiter) {
        this.answerTabulation = answerTabulation;
        this.answerArabicNumerationEnable = answerArabicNumerationEnable;
        this.questionDelimiter = questionDelimiter;
    }

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
                emptyIfNull(questionDelimiter),
                questionPrefix + question.getText(),
                answersString);
    }

    private String answerToString(Answer answer, int answerNumber) {
        var answerNumberPrefix = answerArabicNumerationEnable ? String.format("%d. ", answerNumber + 1) : "";
        return String.format("%s%s%s%n", emptyIfNull(answerTabulation), answerNumberPrefix, answer.getText());
    }
}
