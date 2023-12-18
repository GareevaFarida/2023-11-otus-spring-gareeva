package ru.otus.spring.hw02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw02.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements ru.otus.spring.hw02.service.StudentService {

    private final IOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt("Please input your first name");
        var lastName = ioService.readStringWithPrompt("Please input your last name");
        return new Student(firstName, lastName);
    }
}
