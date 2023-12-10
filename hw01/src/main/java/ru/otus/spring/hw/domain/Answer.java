package ru.otus.spring.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Answer {

    private String text;

    private boolean isCorrect;

}
