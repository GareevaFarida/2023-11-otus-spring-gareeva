package ru.otus.hw.consumer.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.consumer.feign.RandomIntService;

import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class ConsumerControllerTest {

    @MockBean
    private RandomIntService randomIntService;

    @Autowired
    private ConsumerController controller;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void reset() {
        circuitBreakerRegistry.circuitBreaker("myCircuitBreaker").reset();
    }

    @DisplayName("")
    @SneakyThrows
    @Test
    public void circuitBreakerResearchTest() {
        var myBreaker = circuitBreakerRegistry.circuitBreaker("myCircuitBreaker");
        var myBreakerConfig = myBreaker.getCircuitBreakerConfig();

        //получим из настроек количество плохих запросов, после чего брекер разомкнется
        int badRequests = myBreakerConfig.getSlidingWindowSize();
        when(randomIntService.getRandomInt(100)).thenThrow(new RuntimeException("Что-то пошло не так..."));
        IntStream.range(0, badRequests - 1).boxed().forEach(val -> {
            //выполняем плохие запросы
            controller.getCount(100);
            Assertions.assertThat(myBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        });
        //выполняем последний плохой запрос, после которого брекер должен наконец-то разомкнуться
        controller.getCount(100);
        Assertions.assertThat(myBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        when(randomIntService.getRandomInt(99)).thenReturn(98);
        //запустим хороший запрос и убедимся, что он не пройдет
        controller.getCount(99);
        Assertions.assertThat(myBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        //получим из настроек время, которое брекер находится в разомкнутом состоянии
        var waitIntervalFunctionInOpenState = myBreakerConfig.getWaitIntervalFunctionInOpenState();
        long waitIntervalFunctionInOpenStateMilliSec = waitIntervalFunctionInOpenState.apply(1);
        int openStateTimeMinusOneSecond = (int) (waitIntervalFunctionInOpenStateMilliSec / 1000 - 1);
        log.info("Дадим кукушке поспать {} секунд, при этом ей хотелось бы поспать на 1 секунду больше, но мы не дали",
                openStateTimeMinusOneSecond);
        Thread.sleep(openStateTimeMinusOneSecond * 1000L);

        //запустим хороший запрос и убедимся, что кукушка не выспалась и его не пропустит
        controller.getCount(99);
        Assertions.assertThat(myBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        log.info("Дадим кукушке поспать последнюю 1 секунду");
        Thread.sleep(1000);
        Assertions.assertThat(myBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        //снова запустим хороший запрос и убедимся, что брекер сомкнулся, т.к. кукушка выспалась
        controller.getCount(99);
        Assertions.assertThat(myBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }
}
