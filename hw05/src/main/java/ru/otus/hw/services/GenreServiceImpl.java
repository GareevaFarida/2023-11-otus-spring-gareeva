package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public Genre insert(String name) {
        return save(0L, name);
    }

    @Override
    public Genre update(long id, String name) {
        return save(id, name);
    }

    private Genre save(long id, String name) {
        Genre genre = new Genre(id, name);
        return genreRepository.save(genre);
    }
}
