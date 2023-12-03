package ru.otus.spring.hw.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.utils.StringUtils;

import java.util.StringJoiner;

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
    private final boolean answerArabicNumeration;

    /**
     * Строка-разделитель одного блока вопроса с вариантами ответов от другого блока.
     */
    private final String questionDelimiter;

    @Override
    public String apply(Question question, String questionPrefix) {
        if (question == null
                || question.getText() == null) {
            return StringUtils.emptyIfNull(questionPrefix);
        }
        if (question.getAnswers() == null
                || question.getAnswers().isEmpty()) {
            return StringUtils.emptyIfNull(questionPrefix) + question.getText();
        }
        StringJoiner answerJoiner = new StringJoiner(StringUtils.EMPTY);
        for (int answerNumber = 0; answerNumber < question.getAnswers().size(); answerNumber++) {
            answerJoiner.add(StringUtils.emptyIfNull(answerTabulation));
            if (answerArabicNumeration) {
                answerJoiner.add(String.valueOf(answerNumber + 1))
                        .add(". ");
            }
            answerJoiner.add(question.getAnswers().get(answerNumber).getText())
                    .add(System.lineSeparator());
        }
        if (!StringUtils.isEmpty(questionDelimiter)) {
            answerJoiner.add(questionDelimiter);
        }
        return String.format("%s%n%s%n", question.getText(), answerJoiner);
    }
}
