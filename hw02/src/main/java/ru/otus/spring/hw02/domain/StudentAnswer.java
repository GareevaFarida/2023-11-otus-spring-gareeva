package ru.otus.spring.hw02.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
public class StudentAnswer {
    /**
     * Вопрос.
     */
    private final Question question;

    /**
     * Ответы, выбранные студентом.
     */
    private final Set<Integer> checkedAnswers;

    public boolean isCorrect() {
        /*Поскольку вопрос может содержать несколько правильных ответов,
        ответ будет засчитан, если выбраны только все правильные ответы.*/
        List<Answer> answers = question.getAnswers();
        var resultList = IntStream.range(0, answers.size())
                .boxed()
                //Проверяем, что ответ, отмеченный в вопроснике как правильный, присутствует в списке ответов студента,
                //и ответ, отмеченный в вопроснике как неверный, отсутствует в списке ответов студента.
                .map(i -> answers.get(i).isCorrect() == checkedAnswers.contains(i + 1))
                //В случае правильного ответа студента здесь будут только значения true в количестве ответов в опроснике
                .distinct()
                .collect(Collectors.toList());
        return resultList.size() == 1
                && Boolean.TRUE.equals(resultList.get(0));
    }
}
