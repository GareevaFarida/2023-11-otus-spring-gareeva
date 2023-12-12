package ru.otus.spring.hw02.service;

import ru.otus.spring.hw02.domain.Question;

public interface QuestionFormatter {
    String apply(Question question, String questionPrefix);
}
