package ru.otus.spring.hw02.utils;

public final class StringUtils {
    public static final String EMPTY = "";

    public static String emptyIfNull(String str) {
        return str == null ? EMPTY : str;
    }

    public static boolean isEmpty(String str) {
        return str == null || EMPTY.equals(str);
    }
}
