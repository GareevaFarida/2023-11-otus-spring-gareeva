package ru.otus.spring.hw03.dao;

import ru.otus.spring.hw03.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
