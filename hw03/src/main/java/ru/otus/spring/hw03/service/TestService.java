package ru.otus.spring.hw03.service;

import ru.otus.spring.hw03.domain.Student;
import ru.otus.spring.hw03.domain.TestResult;

public interface TestService {

    TestResult executeTestFor(Student student);
}
