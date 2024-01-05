package ru.otus.spring.hw04.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.hw04.domain.Student;

import java.util.stream.Stream;

@SpringBootTest
@DisplayName("Unit тест корректности создания объекта Student в классе StudentServiceImpl")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestPropertySource(properties = "spring.shell.interactive.enabled=false")
public class StudentServiceTest {
    @Import(StudentServiceImpl.class)
    @Configuration
    static class NestedConfiguration {
    }

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
