package ru.otus.spring.hw02.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

@Service
public class IOStreamsService implements IOService {
    private final PrintStream printStream;
    private final PrintStream printErrorStream;
    private final BufferedReader bufferedReader;

    public IOStreamsService(
            @Value("#{ T(System).out}") PrintStream printStream,
            @Value("#{ T(System).in}") InputStream inputStream,
            @Value("#{ T(System).err}") PrintStream printErrorStream
    ) {
        this.printStream = printStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
        String str;
        try {
            str = bufferedReader.readLine();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
