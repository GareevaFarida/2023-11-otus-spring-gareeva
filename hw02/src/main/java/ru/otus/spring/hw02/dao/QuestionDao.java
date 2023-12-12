package ru.otus.spring.hw02.dao;

import ru.otus.spring.hw02.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
