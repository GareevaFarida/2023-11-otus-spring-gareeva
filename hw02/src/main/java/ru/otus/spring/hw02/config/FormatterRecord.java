package ru.otus.spring.hw02.config;

public record FormatterRecord(
        String answerTabulation,
        boolean answerArabicNumerationEnable,
        String questionDelimiter
) {
}
