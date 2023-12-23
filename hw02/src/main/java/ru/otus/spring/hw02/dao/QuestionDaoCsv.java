package ru.otus.spring.hw02.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw02.config.TestFileNameProvider;
import ru.otus.spring.hw02.dao.dto.QuestionDto;
import ru.otus.spring.hw02.domain.Question;
import ru.otus.spring.hw02.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        char separator = '*';
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName())) {
            List<QuestionDto> dtoList = new CsvToBeanBuilder<QuestionDto>(
                    new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
                    .withType(QuestionDto.class)
                    .withSeparator(separator)
                    .build()
                    .parse();
            return dtoList.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }

}
