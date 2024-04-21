package ru.otus.hw.hw15.services.educationservices;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw15.models.Baby;
import ru.otus.hw.hw15.models.Child;

@Slf4j
@Service
public class Kindergarten {
    @SneakyThrows
    public Child teach(Baby baby) {
        while (baby.getAge() < 7) {
            Thread.sleep(100);
            baby.happyBirthday();
            log.info("{} провел в детском саду еще один год", baby.getName());
        }
        return new Child(baby);
    }

}
