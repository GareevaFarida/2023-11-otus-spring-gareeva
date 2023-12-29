package ru.otus.spring.hw02.service;

import java.util.Set;

public interface IOService {
    void printLine(String s);

    void printError(String s);

    void printFormattedLine(String s, Object... args);

    String getLine();

    String readStringWithPrompt(String prompt);

    Set<Integer> readIntegerSetInRangeWithDelimiterAndPrompt(int min,
                                                             int max,
                                                             String delimiter,
                                                             String prompt);
}
