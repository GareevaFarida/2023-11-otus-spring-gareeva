package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

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
        var books = bookRepository.findAllByAuthor_Id(id);
        books.forEach(b -> bookService.deleteById(b.getId()));
    }

    @Override
    @Transactional
    public AuthorDto insert(String name) {
        return save(null, name);
    }

    @Override
    @Transactional
    public AuthorDto update(String id, String name) {
        var authorOptionalBeforeUpdate = authorRepository.findById(id);
        if (authorOptionalBeforeUpdate.isEmpty()) {
            throw new EntityNotFoundException("Не найден автор с id = %s".formatted(id));
        }
        var authorDto = save(id, name);
        var author = modelMapper.map(authorDto, Author.class);
        var books = bookRepository.findAllByAuthor_Id(id);
        books.forEach(b -> b.setAuthor(author));
        bookRepository.saveAll(books);
        return authorDto;
    }

    private AuthorDto save(String id, String name) {
        Author author = new Author(id, name);
        var savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, AuthorDto.class);
    }

}
