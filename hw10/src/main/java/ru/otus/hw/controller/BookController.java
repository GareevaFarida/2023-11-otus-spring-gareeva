package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("api/v1/books")
    public ResponseEntity<List<BookDto>> showAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @DeleteMapping("api/v1/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok("Successfully deleted book with id = %d".formatted(id));
    }

    @GetMapping("api/v1/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long id) {
        var bookDtoOptional = bookService.findBookById(id);
        if (bookDtoOptional.isPresent()) {
            return ResponseEntity.ok(bookDtoOptional.get());
        }
        throw new EntityNotFoundException("Book with id=%d not found".formatted(id));
    }

    @PostMapping(value = "api/v1/books", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> insertBook(@Valid
                                              @RequestBody
                                                      BookDto book,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(book);
        }
        var savedBook = bookService.insert(
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(value = "api/v1/books", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> updateBook(@Valid
                                              @RequestBody
                                                      BookDto book,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(book);
        }
        var savedBook = bookService.update(book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());
        return ResponseEntity.ok(savedBook);
    }
}
