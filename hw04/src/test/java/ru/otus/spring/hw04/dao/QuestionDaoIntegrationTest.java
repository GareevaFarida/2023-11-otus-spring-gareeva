package ru.otus.spring.hw04.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.hw04.config.TestFileNameProvider;

@DisplayName("Интеграционный тест QuestionDao")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class QuestionDaoIntegrationTest {

    @Configuration
    @ComponentScan(
            {"ru.otus.spring.hw04.config",
                    "ru.otus.spring.hw04.dao"}
    )
    static class NestedConfigureation {
        @Bean
        TestFileNameProvider fileNameProvider() {
            return () -> "test-for-students1.csv";
        }
    }

    @Autowired
    QuestionDao questionDao;

    @Test
    @DisplayName("Вычитывает вопросы из ресурсов папки test и сравнивает их количество с заданным числом")
    public void testParsingFile() {
        Assertions.assertEquals(7, questionDao.findAll().size());
    }
}
