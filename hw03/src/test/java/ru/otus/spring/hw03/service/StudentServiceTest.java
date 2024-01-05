package ru.otus.spring.hw03.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.otus.spring.hw03.domain.Student;

import java.util.stream.Stream;

@DisplayName("Unit тест корректности создания объекта Student в классе StudentServiceImpl")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class StudentServiceTest {
    private static LocalizedIOService ioService;
    private static StudentService studentService;

    @BeforeAll
    public static void init() {
        ioService = Mockito.mock(LocalizedIOService.class);
        studentService = new StudentServiceImpl(ioService);
    }

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
