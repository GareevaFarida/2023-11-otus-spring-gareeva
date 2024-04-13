package ru.otus.hw.hw15.models;

public class Baby extends Human {
    public Baby(String name) {
        super(name);
    }

    private Baby(String name, boolean magicalAbility) {
        super(name, magicalAbility);
    }

    public static Baby createWizard(String name) {
        return new Baby(name, true);
    }
}
