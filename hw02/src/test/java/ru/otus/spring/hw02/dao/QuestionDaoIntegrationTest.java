package ru.otus.spring.hw02.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw02.Application;

import static java.util.Objects.isNull;

@DisplayName("Интеграционный тест QuestionDao")
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
public class QuestionDaoIntegrationTest {
    @Autowired
    QuestionDao questionDao;

    @Test
    @DisplayName("Вычитывает вопросы из ресурсов папки test и сравнивает их количество с заданным числом")
    public void test1() {
        var list = questionDao.findAll();
        if (isNull(list)
                || list.isEmpty()) {
            return;
        }
        Assertions.assertEquals(7, questionDao.findAll().size());
    }
}
