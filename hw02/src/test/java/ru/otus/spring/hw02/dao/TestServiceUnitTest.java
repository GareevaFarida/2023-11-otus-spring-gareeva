package ru.otus.spring.hw02.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw02.config.AppProperties;
import ru.otus.spring.hw02.config.TestFileNameProvider;
import ru.otus.spring.hw02.domain.Answer;
import ru.otus.spring.hw02.domain.Question;
import ru.otus.spring.hw02.domain.Student;
import ru.otus.spring.hw02.domain.TestResult;
import ru.otus.spring.hw02.service.IOService;
import ru.otus.spring.hw02.service.QuestionFormatter;
import ru.otus.spring.hw02.service.TestServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@DisplayName("Unit тест сервиса TestService c mock")
public class TestServiceUnitTest {
    private final String delimiter = " ";
    private final String prompt = "Enter number/numbers of correct answers with space delimiter";

    @Test
    @DisplayName("Вычитывает вопросы из ресурсов папки test и сравнивает их количество с заданным числом")
    public void testParsingFile() {
        TestFileNameProvider testFileNameProvider = Mockito.mock(AppProperties.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("test-for-students1.csv");
        QuestionDao questionDao = new QuestionDaoCsv(testFileNameProvider);
        Assertions.assertEquals(7, questionDao.findAll().size());
    }


    @Test
    @DisplayName("Тестирует корректность проверки ответов")
    public void testServiceTest() {

        Question question = new Question("Any question?", Arrays.asList(
                new Answer("answer1", true),
                new Answer("answer2", false),
                new Answer("answer3", true))
        );
        Set<Integer> answersFromIOService = new HashSet<>();
        answersFromIOService.add(1);
        answersFromIOService.add(3);
        checkResult(question, answersFromIOService, true);

        answersFromIOService.clear();
        answersFromIOService.add(1);
        checkResult(question, answersFromIOService, false);

        answersFromIOService.clear();
        answersFromIOService.add(3);
        checkResult(question, answersFromIOService, false);

        answersFromIOService.clear();
        answersFromIOService.add(1);
        answersFromIOService.add(2);
        answersFromIOService.add(3);
        checkResult(question, answersFromIOService, false);

        answersFromIOService.clear();
        answersFromIOService.add(2);
        checkResult(question, answersFromIOService, false);

        answersFromIOService.clear();
        checkResult(question, answersFromIOService, false);
    }

    public void checkResult(Question question, Set<Integer> answersFromIOService, boolean expectedResult) {
        IOService ioService = Mockito.mock(IOService.class);
        QuestionDao questionDao = Mockito.mock(QuestionDao.class);
        QuestionFormatter questionFormatter = Mockito.mock(QuestionFormatter.class);
        TestServiceImpl testService = new TestServiceImpl(ioService, questionDao, questionFormatter);
        TestResult testResult = createTestResult(new Student("firstname", "secondname"));
        Mockito.when(ioService.readIntegerSetInRangeWithDelimiterAndPrompt(1,
                question.getAnswers().size()
                , delimiter,
                prompt))
                .thenReturn(answersFromIOService);
        testService.addAnswer(testResult, question);
        int expected = expectedResult ? 1 : 0;
        Assertions.assertEquals(expected, testResult.getRightAnswersCount());
    }

    private static TestResult createTestResult(Student student) {
        return TestResult.builder()
                .student(student)
                .testingTime(LocalDateTime.now())
                .studentAnswers(new ArrayList<>())
                .build();
    }
}
