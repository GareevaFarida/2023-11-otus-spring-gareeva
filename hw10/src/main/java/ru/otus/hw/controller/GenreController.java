package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @GetMapping("/api/v1/genres")
    public ResponseEntity<List<GenreDto>> getAll() {
        List<GenreDto> genres = service.findAll();
        return ResponseEntity.ok(genres);
    }
}
