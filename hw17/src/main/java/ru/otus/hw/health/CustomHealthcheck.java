package ru.otus.hw.health;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomHealthcheck implements HealthIndicator {
    private final BookRepository bookRepository;

    private final HealthCheckSettings checkSettings;

    @Override
    public Health health() {
        int seconds = checkSettings.getCreateNewBookCheckingInterval();
        try {
            Date someSecondBefore = new Date(System.currentTimeMillis() - seconds * 1000L);
            long count = bookRepository.countAllByCreatedAfter(someSecondBefore);
            if (count > 0) {
                return Health.up()
                        .withDetail("comment",
                                String.format("Наше приложение популярно! "
                                        + "За последние %d секунд появились %d новые книги!", seconds, count)
                        )
                        .build();
            } else {
                return Health.down()
                        .withDetail("comment", "Все пропало! Нет ни одной новой книги за последние %d секунд!"
                                .formatted(seconds))
                        .build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("comment", ex.getMessage())
                    .build();
        }
    }
}
