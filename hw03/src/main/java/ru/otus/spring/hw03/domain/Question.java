package ru.otus.spring.hw03.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Question {

    private String text;

    private List<Answer> answers;
}
