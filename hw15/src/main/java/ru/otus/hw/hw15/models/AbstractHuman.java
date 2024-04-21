package ru.otus.hw.hw15.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

@Slf4j
@Getter
public abstract class AbstractHuman {
    private final boolean magicalAbility;

    private int age;

    private String name;

    public AbstractHuman(String name, boolean magicalAbility) {
        this.name = name;
        this.magicalAbility = magicalAbility;
        this.age = 0;
    }

    public AbstractHuman(AbstractHuman abstractHuman) {
        this.name = abstractHuman.getName();
        this.age = abstractHuman.getAge();
        this.magicalAbility = abstractHuman.isMagicalAbility();
    }

    public AbstractHuman(String name) {
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
