package ru.otus.spring.hw03.config;

public record FormatterProperties(
        String answerTabulation,
        boolean answerArabicNumerationEnable,
        String questionDelimiter
) {
}
