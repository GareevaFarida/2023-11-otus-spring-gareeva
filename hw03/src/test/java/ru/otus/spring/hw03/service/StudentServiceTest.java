package ru.otus.spring.hw03.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import ru.otus.spring.hw03.domain.Student;

@DisplayName("Unit тест корректности создания объекта Student в классе StudentServiceImpl")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentServiceTest {
    private LocalizedIOService ioService;
    private StudentService studentService;

    @BeforeAll
    public void init() {
        this.ioService = Mockito.mock(LocalizedIOService.class);
        this.studentService = new StudentServiceImpl(ioService);
    }

    @Test
    public void test1() {
        test("Иван", "Иванов");
    }

    @Test
    public void test2() {
        test("Иван", null);
    }

    @Test
    public void test3() {
        test(null, "Иванов");
    }

    @Test
    public void test4() {
        test(null, null);
    }

    private void test(String firstname, String lastname) {
        Mockito.when(ioService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn(firstname);
        Mockito.when(ioService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn(lastname);
        Student studentFromService = studentService.determineCurrentStudent();
        Student simpleStudent = new Student(firstname, lastname);
        Assertions.assertEquals(simpleStudent.getFullName(), studentFromService.getFullName());
    }
}
