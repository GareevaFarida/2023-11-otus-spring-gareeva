package ru.otus.hw.batch.cache;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthorCache implements CacheExtractor<Author> {
    private Map<Long, Author> cache = new HashMap<>();

    @Override
    public Author getEntityById(long id) {
        if (!cache.containsKey(id)) {
            throw new IllegalArgumentException("Not found Author with id = %d".formatted(id));
        }
        return cache.get(id);
    }

    @Override
    public void put(long id, Author entity) {
        cache.put(id, entity);
    }
}
