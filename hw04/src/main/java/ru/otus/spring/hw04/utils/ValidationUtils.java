package ru.otus.spring.hw04.utils;

import ru.otus.spring.hw04.domain.Answer;
import ru.otus.spring.hw04.domain.StudentAnswer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValidationUtils {
    public static boolean isAnswerValid(StudentAnswer studentAnswer) {
        List<Answer> answers = studentAnswer.getQuestion().getAnswers();
        var resultList = IntStream.range(0, answers.size())
                .boxed()
                .map(i -> answers.get(i).isCorrect() == studentAnswer.getCheckedAnswers().contains(i + 1))
                .distinct()
                .collect(Collectors.toList());
        return resultList.size() == 1
                && Boolean.TRUE.equals(resultList.get(0));
    }
}
