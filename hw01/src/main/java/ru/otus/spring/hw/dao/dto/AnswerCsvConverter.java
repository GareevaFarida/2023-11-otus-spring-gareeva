package ru.otus.spring.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.spring.hw.domain.Answer;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("\\$");
        if (valueArr.length == 1) {
            return new Answer(valueArr[0], false);
        } else {
            return new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));
        }
    }
}
