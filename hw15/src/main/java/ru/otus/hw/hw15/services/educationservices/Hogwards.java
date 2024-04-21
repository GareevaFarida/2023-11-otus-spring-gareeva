package ru.otus.hw.hw15.services.educationservices;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw15.models.Child;
import ru.otus.hw.hw15.models.Wizard;

@Slf4j
@Service
public class Hogwards {
    @SneakyThrows
    public Wizard teach(Child child) {
        int level = 1;
        while (level < 8) {
            Thread.sleep(100);
            child.happyBirthday();
            log.info("{} закончил {} курс Хогвардса", child.getName(), level);
            level++;
        }
        log.info("Поздравляем молодого волшебника {}!", child.getName());
        return new Wizard(child);
    }
}
