package ru.otus.spring.hw03.service;

import ru.otus.spring.hw03.domain.Question;

public interface QuestionFormatter {
    String apply(Question question, String questionPrefix);
}
