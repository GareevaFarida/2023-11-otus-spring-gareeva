package dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.hw03.domain.Answer;
import ru.otus.spring.hw03.domain.Question;
import ru.otus.spring.hw03.domain.StudentAnswer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ru.otus.spring.hw03.utils.ValidationUtils.isAnswerValid;

@DisplayName("Unit тест корректности проверки ответа")
public class TestServiceUnitTest {

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

        answersFromIOService.clear();
        answersFromIOService.add(1);
        answersFromIOService.add(2);
        checkResult(question, answersFromIOService, false);
    }

    public void checkResult(Question question, Set<Integer> inputedAnswers, boolean expectedResult) {
        StudentAnswer studentAnswer = new StudentAnswer(question, inputedAnswers);
        Assertions.assertEquals(expectedResult, isAnswerValid(studentAnswer));
    }
}
