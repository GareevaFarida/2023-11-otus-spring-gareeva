package ru.otus.spring.hw02.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Student {
    private final String firstname;

    private final String secondname;

    public String getFullName() {
        return String.format("%s %s", firstname, secondname);
    }
}
