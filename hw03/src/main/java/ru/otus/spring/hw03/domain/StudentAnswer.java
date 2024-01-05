package ru.otus.spring.hw03.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class StudentAnswer {

    private final Question question;

    private final Set<Integer> checkedAnswers;
}
