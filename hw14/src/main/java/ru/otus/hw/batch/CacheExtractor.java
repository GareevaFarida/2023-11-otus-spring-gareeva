package ru.otus.hw.batch;

public interface CacheExtractor<T> {
    T getEntityById(long id);
}
