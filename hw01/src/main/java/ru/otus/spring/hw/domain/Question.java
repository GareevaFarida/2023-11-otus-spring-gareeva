package ru.otus.spring.hw.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.otus.spring.hw.dao.dto.AnswerCsvConverter;

import java.util.List;
import java.util.StringJoiner;

@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @CsvBindByName
    private String text;

    @CsvBindAndSplitByName(elementType = Answer.class, splitOn = "\\|", converter = AnswerCsvConverter.class)
    private List<Answer> answers;


    @Override
    public String toString() {
        if (answers == null) {
            return text;
        }
        StringJoiner joiner = new StringJoiner("");
        for (int answerNumber = 0; answerNumber < answers.size(); answerNumber++) {
            joiner.add("    ")
                    .add(String.valueOf(answerNumber + 1))
                    .add(". ")
                    .add(answers.get(answerNumber).getText())
                    .add(System.lineSeparator());
        }
        return String.format("%s%n%s", text, joiner);
    }
}
