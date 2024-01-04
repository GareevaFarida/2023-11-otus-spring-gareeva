package ru.otus.spring.hw04.service;

import ru.otus.spring.hw04.domain.Student;
import ru.otus.spring.hw04.domain.TestResult;

public interface TestService {

    TestResult executeTestFor(Student student);
}
