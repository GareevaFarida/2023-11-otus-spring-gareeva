package ru.otus.spring.hw02.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties implements TestFileNameProvider, TestConfig, Formatter {

    private final String testFileName;

    private final int minAcceptCount;

    private final FormatterRecord formatterRecord;

    public AppProperties(@Value("${test.filename:dao/test-for-students.csv}") String testFileName,
                         @Value("${test.min-accept-count:3}") int minAcceptCount,
                         @Value("${test.formatter.tabulation:    }") String answerTabulation,
                         @Value("${test.formatter.answer-arabic-numeration-enable:true}")
                                 boolean answerArabicNumerationEnable,
                         @Value("${test.formatter.question-delimiter:}") String questionDelimiter
    ) {
        this.testFileName = testFileName;
        this.minAcceptCount = minAcceptCount;
        this.formatterRecord = new FormatterRecord(
                answerTabulation,
                answerArabicNumerationEnable,
                questionDelimiter);
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return minAcceptCount;
    }

    @Override
    public FormatterRecord getFormatter() {
        return formatterRecord;
    }
}
