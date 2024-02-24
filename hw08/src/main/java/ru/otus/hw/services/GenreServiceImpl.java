package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(val -> modelMapper.map(val, GenreDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenreDto> findById(String id) {
        return genreRepository.findById(id)
                .map(val -> modelMapper.map(val, GenreDto.class));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        genreRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GenreDto insert(String name) {
        return save(null, name);
    }

    @Override
    @Transactional
    public GenreDto update(String id, String name) {
        return save(id, name);
    }

    private GenreDto save(String id, String name) {
        Genre genre = new Genre(id, name);
        Genre savedGenre = genreRepository.save(genre);
        return modelMapper.map(genre, GenreDto.class);
    }
}
