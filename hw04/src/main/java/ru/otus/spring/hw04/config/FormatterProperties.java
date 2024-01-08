package ru.otus.spring.hw04.config;

public record FormatterProperties(
        String answerTabulation,
        boolean answerArabicNumerationEnable,
        String questionDelimiter
) {
}
