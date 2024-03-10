package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @GetMapping("/api/v1/authors")
    public ResponseEntity<List<AuthorDto>> getAll() {
        List<AuthorDto> authors = service.findAll();
        return ResponseEntity.ok(authors);
    }
}
