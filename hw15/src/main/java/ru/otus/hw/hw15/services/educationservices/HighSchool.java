package ru.otus.hw.hw15.services.educationservices;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw15.models.Child;
import ru.otus.hw.hw15.models.YoungMan;

@Slf4j
@Service
public class HighSchool {
    @SneakyThrows
    public YoungMan teach(Child child) {
        int level = 5;
        while (level < 12) {
            Thread.sleep(100);
            child.happyBirthday();
            log.info("{} закончил {} класс средней школы", child.getName(), level);
            level++;
        }
        log.info("Поздравляем выпускника школы {}!", child.getName());
        return new YoungMan(child);
    }
}
