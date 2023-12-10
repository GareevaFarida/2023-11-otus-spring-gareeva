package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Question;

public interface QuestionFormatter {
    String apply(Question question, String questionPrefix);
}
