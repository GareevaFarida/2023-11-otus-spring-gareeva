package ru.otus.spring.hw04.service;

import ru.otus.spring.hw04.domain.Question;

public interface QuestionFormatter {
    String apply(Question question, String questionPrefix);
}
