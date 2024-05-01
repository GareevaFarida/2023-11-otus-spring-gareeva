package ru.otus.hw.services;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAll();

    Optional<GenreDto> findById(String id);

    void deleteById(String id);

    GenreDto insert(String name);

    GenreDto update(String id, String name);
}
