package ru.otus.spring.hw.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.otus.spring.hw.utils.StringUtils.emptyIfNull;

@AllArgsConstructor
@Builder
public class QuestionFormatterImpl implements QuestionFormatter {

    /**
     * Отступ перед вариантами ответов.
     */
    private final String answerTabulation;

    /**
     * Признак, что варианты ответов надо нумеровать арабскими цифрами
     */
    private final boolean answerArabicNumerationEnable;

    /**
     * Строка-разделитель одного блока вопроса с вариантами ответов от другого блока.
     */
    private final String questionDelimiter;

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

        return String.format("%s%n%s%s%n", question.getText(), answersString, emptyIfNull(questionDelimiter));
    }

    private String answerToString(Answer answer, int answerNumber) {
        var answerNumberPrefix = answerArabicNumerationEnable ? String.format("%d. ", answerNumber + 1) : "";
        return String.format("%s%s%s%n", emptyIfNull(answerTabulation), answerNumberPrefix, answer.getText());
    }
}
