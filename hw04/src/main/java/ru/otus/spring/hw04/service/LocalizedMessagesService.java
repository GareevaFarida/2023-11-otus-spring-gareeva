package ru.otus.spring.hw04.service;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
