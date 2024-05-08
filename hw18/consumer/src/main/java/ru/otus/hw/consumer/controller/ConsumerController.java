package ru.otus.hw.consumer.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.consumer.feign.RandomIntService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConsumerController {
    private final RandomIntService randomIntService;


    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "recoverMethod")
    @GetMapping(value = "count")
    public String getCount(@RequestParam(name = "upper-bound") int upperBound) {
        int count = randomIntService.getRandomInt(upperBound);
        log.info("Успешно получили значение {}", count);
        return "Кукушка сказала 'ку-ку' %d раз".formatted(count);
    }

    private String recoverMethod(Exception ex) {
        log.warn("Кукушка сломалась {}", ex.getMessage());
        return "Кукушка сломалась: %s".formatted(ex.getMessage());
    }
}
