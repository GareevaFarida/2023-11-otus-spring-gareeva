package ru.otus.spring.hw03.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IOStreamsService implements IOService {
    private static final int MAX_COUNT_ATTEMPTS = 5;

    private final PrintStream printStream;

    private final PrintStream printErrorStream;

    private final Scanner scanner;

    public IOStreamsService(
            @Value("#{ T(System).out}") PrintStream printStream,
            @Value("#{ T(System).in}") InputStream inputStream,
            @Value("#{ T(System).err}") PrintStream printErrorStream
    ) {
        this.printStream = printStream;
        this.scanner = new Scanner(new InputStreamReader(inputStream));
        this.printErrorStream = printErrorStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printError(String s) {
        printErrorStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String getLine() {
        return scanner.nextLine();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return getLine();
    }

    @Override
    public Set<Integer> readIntegerSetInRangeWithDelimiterAndPrompt(int min,
                                                                    int max,
                                                                    String delimiter,
                                                                    String promptDelimiter,
                                                                    String promptError) {
        int attemptNumber = 0;
        while (attemptNumber < MAX_COUNT_ATTEMPTS) {
            String inputNumberLine = readStringWithPrompt(promptDelimiter).trim();
            Set<Integer> numbers;
            try {
                numbers = Arrays.stream(inputNumberLine.split(delimiter))
                        .map(Integer::valueOf)
                        .collect(Collectors.toSet());
                validateNumbers(numbers, min, max);
                return numbers;
            } catch (IllegalArgumentException ex) {
                attemptNumber++;
                printError(promptError);
            }
        }
        throw new IllegalArgumentException("Error while reading answer.");
    }

    private void validateNumbers(Set<Integer> numbers, int min, int max) {
        if (numbers.isEmpty()) {
            return;
        }
        if (numbers.stream()
                .anyMatch(val -> val > max || val < min)) {
            throw new IllegalArgumentException();
        }
    }
}
