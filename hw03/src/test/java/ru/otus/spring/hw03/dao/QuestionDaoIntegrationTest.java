package ru.otus.spring.hw03.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw03.config.AppProperties;
import ru.otus.spring.hw03.config.TestFileNameProvider;

@DisplayName("Интеграционный тест QuestionDao")
public class QuestionDaoIntegrationTest {
    @Test
    @DisplayName("Вычитывает вопросы из ресурсов папки test и сравнивает их количество с заданным числом")
    public void testParsingFile() {
        TestFileNameProvider testFileNameProvider = Mockito.mock(AppProperties.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("test-for-students1.csv");
        QuestionDao questionDao = new QuestionDaoCsv(testFileNameProvider);
        Assertions.assertEquals(7, questionDao.findAll().size());
    }
}
