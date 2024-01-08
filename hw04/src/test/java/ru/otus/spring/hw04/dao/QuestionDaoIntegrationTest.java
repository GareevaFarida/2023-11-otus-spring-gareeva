package ru.otus.spring.hw04.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.hw04.config.TestFileNameProvider;

@DisplayName("Интеграционный тест QuestionDao")
@SpringBootTest(classes = {QuestionDaoCsv.class})
public class QuestionDaoIntegrationTest {

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Test
    @DisplayName("Вычитывает вопросы из ресурсов папки test и сравнивает их количество с заданным числом")
    public void testParsingFile() {
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn("test-for-students1.csv");
        Assertions.assertEquals(7, questionDao.findAll().size());
    }
}
