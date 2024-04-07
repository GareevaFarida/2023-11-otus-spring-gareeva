package ru.otus.hw.batch.cache;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;

import java.util.HashMap;
import java.util.Map;

@Component
public class GenreCache implements CacheExtractor<Genre> {
    private Map<Long, Genre> cache = new HashMap<>();

    @Override
    public Genre getEntityById(long id) {
        if (!cache.containsKey(id)) {
            throw new IllegalArgumentException("Not found Genre with id = %d".formatted(id));
        }
        return cache.get(id);
    }

    @Override
    public void put(long id, Genre entity) {
        cache.put(id, entity);
    }
}
