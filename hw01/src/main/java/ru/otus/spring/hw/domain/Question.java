package ru.otus.spring.hw.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.hw.dao.dto.AnswerCsvConverter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @CsvBindByName
    private String text;

    @CsvBindAndSplitByName(elementType = Answer.class, splitOn = "\\|", converter = AnswerCsvConverter.class)
    private List<Answer> answers;
}
