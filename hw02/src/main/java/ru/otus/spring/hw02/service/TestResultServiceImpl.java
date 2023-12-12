package ru.otus.spring.hw02.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw02.config.TestFileNameProvider;
import ru.otus.spring.hw02.domain.Answer;
import ru.otus.spring.hw02.domain.Question;
import ru.otus.spring.hw02.domain.Student;
import ru.otus.spring.hw02.domain.StudentAnswer;
import ru.otus.spring.hw02.domain.TestResult;
import ru.otus.spring.hw02.exceptions.AnswerReadException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class TestResultServiceImpl implements TestResultService {

    private final IOService ioService;

    private final TestFileNameProvider testFileNameProvider;

    @Value("${test.min-accept-count:3}")
    private int minAcceptCount;

    @Override
    public TestResult createTestResult() {
        Student student = getStudentInfo();
        return TestResult.builder()
                .student(student)
                .testingTime(LocalDateTime.now())
                .testSourcePath(testFileNameProvider.getTestFileName())
                .studentAnswers(new ArrayList<>())
                .build();
    }

    private Student getStudentInfo() {
        ioService.printLine("Enter your first name, please");
        String firstName = ioService.getLine();
        ioService.printLine("Enter your last name, please");
        String secondName = ioService.getLine();
        return new Student(firstName, secondName);
    }

    @Override
    public void addAnswer(TestResult testResult, Question question) throws AnswerReadException {
        var answer = ioService.getLine().trim();
        Set<Integer> answerList;
        try {
            answerList = Arrays.stream(answer.split(" "))
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());
            validateAnswerList(answerList, question.getAnswers().size());
        } catch (IllegalArgumentException ex) {
            throw new AnswerReadException("Answers can be only natural numbers! Try again.");
        } catch (IndexOutOfBoundsException ex) {
            throw new AnswerReadException(ex.getMessage());
        }
        var studentAnswer = new StudentAnswer(question, answerList);
        testResult.addStudentAnswer(studentAnswer);
    }

    @Override
    public void checkResults(@NonNull TestResult testResult) {
        if (isNull(testResult.getStudentAnswers())
                || isEmpty(testResult.getStudentAnswers())) {
            return;
        }
        ioService.printLine("Testing result is");
        int correctAnswersCount = 0;
        var studentAnswersList = testResult.getStudentAnswers();
        for (int questionNum = 0; questionNum < studentAnswersList.size(); questionNum++
        ) {
            var studentAnswer = studentAnswersList.get(questionNum);
            var answers = studentAnswer.getQuestion().getAnswers();
            var checkedAnswers = studentAnswer.getCheckedAnswers();
            var isCorrect = isAnswerCorrect(answers, checkedAnswers);
            if (isCorrect) {
                correctAnswersCount++;
            }
            ioService.printFormattedLine("Answer for question №%d is %scorrect",
                    questionNum + 1,
                    isCorrect?"":"un");
        }
        ioService.printFormattedLine("%s, your result is %d from %d. Your testing is %s!",
                testResult.getStudent().getFirstname(),
                correctAnswersCount,
                studentAnswersList.size(),
                correctAnswersCount >= minAcceptCount ? "successful" : "failed");
    }

    private boolean isAnswerCorrect(List<Answer> answers, Set<Integer> checkedAnswers) {
        var resultList = IntStream.range(0, answers.size())
                .boxed()
                .map(i -> answers.get(i).isCorrect() == checkedAnswers.contains(i + 1))
                .distinct()
                .collect(Collectors.toList());
        return resultList.size() == 1
                && Boolean.TRUE.equals(resultList.get(0));
    }

    private void validateAnswerList(Set<Integer> answerList, int size) {
        if (answerList.isEmpty()) {
            return;
        }
        if (answerList.stream()
                .anyMatch(val -> val > size)) {
            throw new IndexOutOfBoundsException(
                    "Checked answer number is greater than total count of answers! Try again");
        }
    }
}
