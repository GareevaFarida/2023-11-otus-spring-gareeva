package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final MongoTemplate mongoTemplate;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(val -> modelMapper.map(val, AuthorDto.class))
                .toList();
    }

    @Override
    public Optional<AuthorDto> findById(String id) {
        return authorRepository.findById(id)
                .map(val -> modelMapper.map(val, AuthorDto.class));
    }

    @Override
    public void deleteById(String id) {
        authorRepository.deleteById(id);
        bookRepository.deleteAllByAuthor_Id(id);
    }

    @Override
    public AuthorDto insert(String name) {
        var author = save(null, name);
        return modelMapper.map(author, AuthorDto.class);
    }

    @Override
    public AuthorDto update(String id, String name) {
        var authorOptionalBeforeUpdate = authorRepository.findById(id);
        if (authorOptionalBeforeUpdate.isEmpty()) {
            throw new EntityNotFoundException("Не найден автор с id = %s".formatted(id));
        }
        var author = save(id, name);

        Query query = new Query();
        query.addCriteria(Criteria.where("author._id").is(id));
        Update update = Update.update("author.fullName", name);
        mongoTemplate.updateMulti(query, update, Book.class);

        return modelMapper.map(author, AuthorDto.class);
    }

    private Author save(String id, String name) {
        Author author = new Author(id, name);
        return authorRepository.save(author);
    }

}
