package ru.otus.spring.hw03.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Answer {

    private String text;

    private boolean correct;

}
