package ru.otus.hw.hw15.services.educationservices;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw15.models.Child;

@Slf4j
@Service
public class PrimarySchool {
    @SneakyThrows
    public Child teach(Child child) {
        int level = 1;
        while (level < 5) {
            Thread.sleep(100);
            child.happyBirthday();
            log.info("{} закончил {} класс начальной школы", child.getName(), level);
            level++;
        }
        return child;
    }
}
