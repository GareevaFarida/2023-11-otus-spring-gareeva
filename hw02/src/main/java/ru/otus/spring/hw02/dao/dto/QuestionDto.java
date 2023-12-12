package ru.otus.spring.hw02.dao.dto;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import ru.otus.spring.hw02.domain.Answer;
import ru.otus.spring.hw02.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {

    @CsvBindByName
    private String text;

    @CsvBindAndSplitByName(collectionType = ArrayList.class, elementType = Answer.class,
            converter = AnswerCsvConverter.class, splitOn = "\\|")
    private List<Answer> answers;

    public Question toDomainObject() {
        return new Question(text, answers);
    }
}
