package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final MongoTemplate mongoTemplate;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

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
        bookRepository.deleteAllByGenre_Id(id);
    }

    @Override
    @Transactional
    public GenreDto insert(String name) {
        var genre = save(null, name);
        return modelMapper.map(genre, GenreDto.class);
    }

    @Override
    @Transactional
    public GenreDto update(String id, String name) {
        var genreOptionalBeforeUpdate = genreRepository.findById(id);
        if (genreOptionalBeforeUpdate.isEmpty()) {
            throw new EntityNotFoundException("Не найден жанр с id = %s".formatted(id));
        }
        var genre = save(id, name);

        Query query = new Query();
        query.addCriteria(Criteria.where("genre._id").is(id));
        Update update = Update.update("genre.name", name);
        mongoTemplate.updateMulti(query, update, Book.class);

        return modelMapper.map(genre, GenreDto.class);
    }

    private Genre save(String id, String name) {
        Genre genre = new Genre(id, name);
        return genreRepository.save(genre);
    }
}
