package ru.otus.spring.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Answer {

    private String text;

    private boolean isCorrect;

}
