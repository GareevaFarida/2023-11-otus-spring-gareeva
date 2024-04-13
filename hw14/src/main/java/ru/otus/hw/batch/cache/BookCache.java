package ru.otus.hw.batch.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookCache implements CacheExtractor<String> {
    private Map<Long, String> cache = new HashMap<>();

    @Override
    public String getEntityById(long id) {
        if (!cache.containsKey(id)) {
            throw new IllegalArgumentException("Not found Book with id = %d".formatted(id));
        }
        return cache.get(id);
    }

    @Override
    public void put(long id, String bookIdMongo) {
        cache.put(id, bookIdMongo);
    }
}
