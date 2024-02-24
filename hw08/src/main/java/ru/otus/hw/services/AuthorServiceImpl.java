package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(val -> modelMapper.map(val, AuthorDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDto> findById(String id) {
        return authorRepository.findById(id)
                .map(val -> modelMapper.map(val, AuthorDto.class));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        authorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AuthorDto insert(String name) {
        return save(null, name);
    }

    @Override
    @Transactional
    public AuthorDto update(String id, String name) {
        return save(id, name);
    }

    private AuthorDto save(String id, String name) {
        Author author = new Author(id, name);
        return modelMapper.map(authorRepository.save(author), AuthorDto.class);
    }

}
