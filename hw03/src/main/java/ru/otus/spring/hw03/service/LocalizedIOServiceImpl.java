package ru.otus.spring.hw03.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class LocalizedIOServiceImpl implements LocalizedIOService {
    private final LocalizedMessagesService localizedMessagesService;

    private final IOService ioService;

    @Override
    public void printLine(String s) {
        ioService.printLine(s);
    }

    @Override
    public void printError(String s) {
        ioService.printError(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        ioService.printFormattedLine(s, args);
    }

    @Override
    public String getLine() {
        return ioService.getLine();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return ioService.readStringWithPrompt(prompt);
    }

    @Override
    public Set<Integer> readIntegerSetInRangeWithDelimiterAndPrompt(int min,
                                                                    int max,
                                                                    String delimiter,
                                                                    String promptDelimiter,
                                                                    String promptError) {
        return ioService.readIntegerSetInRangeWithDelimiterAndPrompt(min, max, delimiter, promptDelimiter, promptError);
    }

    @Override
    public void printLineLocalized(String code) {
        ioService.printLine(localizedMessagesService.getMessage(code));
    }

    @Override
    public void printErrorLocalized(String s) {
        ioService.printError(localizedMessagesService.getMessage(s));
    }

    @Override
    public void printFormattedLineLocalized(String code, Object... args) {
        ioService.printFormattedLine(localizedMessagesService.getMessage(code, args));
    }

    @Override
    public String readStringWithPromptLocalized(String promptCode) {
        return ioService.readStringWithPrompt(localizedMessagesService.getMessage(promptCode));
    }

    @Override
    public Set<Integer> readIntegerSetInRangeWithDelimiterAndPromptLocalized(int min,
                                                                             int max,
                                                                             String delimiter,
                                                                             String promptDelimiter,
                                                                             String promptError) {
        return ioService.readIntegerSetInRangeWithDelimiterAndPrompt(min,
                max,
                delimiter,
                localizedMessagesService.getMessage(promptDelimiter),
                localizedMessagesService.getMessage(promptError, min, max));
    }

    @Override
    public String getMessage(String code, Object... args) {
        return localizedMessagesService.getMessage(code, args);
    }
}
