package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookService bookService;

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
        var books = bookRepository.findAllByGenre_Id(id);
        books.forEach(b -> bookService.deleteById(b.getId()));
    }

    @Override
    @Transactional
    public GenreDto insert(String name) {
        return save(null, name);
    }

    @Override
    @Transactional
    public GenreDto update(String id, String name) {
        var genreOptionalBeforeUpdate = genreRepository.findById(id);
        if (genreOptionalBeforeUpdate.isEmpty()) {
            throw new EntityNotFoundException("Не найден жанр с id = %s".formatted(id));
        }
        var genreBeforeUpdated = genreOptionalBeforeUpdate.get();
        var genreDto = save(id, name);
        var genre = modelMapper.map(genreDto, Genre.class);
        var books = bookRepository.findAllByGenre(genreBeforeUpdated);
        bookRepository.saveAll(books);
        books.forEach(b -> b.setGenre(genre));
        bookRepository.saveAll(books);
        return genreDto;
    }

    private GenreDto save(String id, String name) {
        Genre genre = new Genre(id, name);
        Genre savedGenre = genreRepository.save(genre);
        return modelMapper.map(savedGenre, GenreDto.class);
    }
}
