package ru.otus.spring.hw04.dao;

import ru.otus.spring.hw04.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
