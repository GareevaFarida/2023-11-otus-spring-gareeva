package ru.otus.hw.health;


import lombok.SneakyThrows;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
public class CustomHealthcheck implements HealthIndicator {
    private final BookRepository bookRepository;

    private long countLast;

    public CustomHealthcheck(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.countLast = bookRepository.count();
    }

    @Override
    @SneakyThrows
    public Health health() {

        long count = bookRepository.count();
        long countBefore = countLast;
        countLast = count;
        boolean isApplicationActive = count > countBefore;
        if (isApplicationActive) {
            return Health.up()
                    .withDetail("comment",
                            String.format("Наше приложение популярно! "
                                            + "С момента последней проверки появились %d новые книги!",
                                    count - countBefore)
                    )
                    .build();
        } else {
            return Health.down()
                    .withDetail("comment", "Все пропало! Нет ни одной новой книги с момента последней проверки!")
                    .build();
        }
    }
}
