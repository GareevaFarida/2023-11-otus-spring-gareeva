package ru.otus.spring.hw01;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.hw01.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {

        //Прописать бины в spring-context.xml и создать контекст на его основе
        var context = new ClassPathXmlApplicationContext("spring-context.xml");
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}