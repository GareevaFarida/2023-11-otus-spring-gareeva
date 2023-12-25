package ru.otus.spring.hw03.config;

public record FormatterRecord(
        String answerTabulation,
        boolean answerArabicNumerationEnable,
        String questionDelimiter
) {
}
