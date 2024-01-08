package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Author insert(String name) {
        return save(0L, name);
    }

    @Override
    public Author update(long id, String name) {
        return save(id, name);
    }

    private Author save(long id, String name) {
        Author author = new Author(id, name);
        return authorRepository.save(author);
    }

}
