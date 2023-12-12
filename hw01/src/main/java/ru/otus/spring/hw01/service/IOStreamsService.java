package ru.otus.spring.hw01.service;

import lombok.AllArgsConstructor;

import java.io.PrintStream;

@AllArgsConstructor
public class IOStreamsService implements IOService {
    private final PrintStream printStream;

    public IOStreamsService() {
        this.printStream = System.out;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }
}
