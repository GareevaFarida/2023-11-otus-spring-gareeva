package ru.otus.spring.hw04.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.hw04.domain.Student;

import java.util.stream.Stream;

@SpringBootTest(classes = StudentServiceImpl.class)
@DisplayName("Интеграционный тест корректности создания объекта Student в классе StudentServiceImpl с контекстом")
//@TestPropertySource(properties = "spring.shell.interactive.enabled=false")
public class StudentServiceTest {

    @MockBean
    private LocalizedIOService ioService;
    @Autowired
    private StudentService studentService;

    @ParameterizedTest
    @MethodSource("generateData")
    public void test(String firstname, String lastname) {
        Mockito.when(ioService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn(firstname);
        Mockito.when(ioService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn(lastname);
        Student studentFromService = studentService.determineCurrentStudent();
        Student simpleStudent = new Student(firstname, lastname);
        Assertions.assertEquals(simpleStudent.getFullName(), studentFromService.getFullName());
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of("Иван", "Иванов"),
                Arguments.of("Иван", null),
                Arguments.of(null, "Иванов"),
                Arguments.of(null, null)
        );
    }
}
