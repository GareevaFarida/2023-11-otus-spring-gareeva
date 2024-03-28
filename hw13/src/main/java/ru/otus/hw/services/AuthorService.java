package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAll();

    Optional<AuthorDto> findById(long id);

    void deleteById(long id);

    AuthorDto insert(String name);

    AuthorDto update(long id, String name);
}
