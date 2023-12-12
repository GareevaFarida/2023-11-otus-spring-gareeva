package ru.otus.spring.hw01.service;

import ru.otus.spring.hw01.domain.Question;

public interface QuestionFormatter {
    String apply(Question question, String questionPrefix);
}
