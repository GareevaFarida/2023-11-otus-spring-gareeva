package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAll();

    Optional<AuthorDto> findById(String id);

    void deleteById(String id);

    AuthorDto insert(String name);

    AuthorDto update(String id, String name);
}
