package ru.otus.spring.hw02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.hw02.service.TestRunnerService;

@ComponentScan
@Configuration
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(Application.class);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}