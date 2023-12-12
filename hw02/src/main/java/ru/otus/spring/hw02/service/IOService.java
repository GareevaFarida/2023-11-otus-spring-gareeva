package ru.otus.spring.hw02.service;

public interface IOService {
    void printLine(String s);

    void printError(String s);

    void printFormattedLine(String s, Object... args);

    String getLine();
}
