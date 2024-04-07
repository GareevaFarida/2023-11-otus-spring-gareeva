package ru.otus.hw.batch.cache;

public interface CacheExtractor<T> {
    T getEntityById(long id);

    void put(long id, T entity);
}
