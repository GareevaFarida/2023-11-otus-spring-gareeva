package ru.otus.spring.hw04.service;

import java.util.Set;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {
    void printLineLocalized(String code);

    void printErrorLocalized(String s);

    void printFormattedLineLocalized(String code, Object... args);

    String readStringWithPromptLocalized(String promptCode);

    Set<Integer> readIntegerSetInRangeWithDelimiterAndPromptLocalized(int min,
                                                                      int max,
                                                                      String delimiter,
                                                                      String promptDelimiter,
                                                                      String promptError);
}
