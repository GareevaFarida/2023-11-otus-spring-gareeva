package ru.otus.spring.hw02.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class StudentAnswer {
    /**
     * Вопрос.
     */
    private final Question question;
    /**
     * Ответы, выбранные студентом.
     */
    private final Set<Integer> checkedAnswers;
}
