package ru.otus.spring.hw04.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.hw04.domain.Student;

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
