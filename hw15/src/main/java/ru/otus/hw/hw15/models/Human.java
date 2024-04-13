package ru.otus.hw.hw15.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

@Slf4j
@Getter
public abstract class Human {
    private final boolean magicalAbility;
    private int age;
    private String name;

    public Human(String name, boolean magicalAbility) {
        this.name = name;
        this.magicalAbility = magicalAbility;
        this.age = 0;
    }

    public Human(Human human) {
        this.name = human.getName();
        this.age = human.getAge();
        this.magicalAbility = human.isMagicalAbility();
    }

    public Human(String name) {
        this.age = 0;
        this.magicalAbility = generateMagicalAbility();
        this.name = name + (this.isMagicalAbility() ? " (WIZARD)" : "");
    }

    public int happyBirthday() {
        log.info("{} исполнилось {} лет!", name, ++age);
        return age;
    }

    private boolean generateMagicalAbility() {
        return RandomUtils.nextInt(1, 10) > 6;
    }

}
