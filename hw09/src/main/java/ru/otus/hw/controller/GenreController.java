package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @GetMapping("/genres")
    public String listPage(Model model) {
        List<GenreDto> genres = service.findAll();
        model.addAttribute("genres", genres);
        return "genre/list";
    }

    @GetMapping("/genres/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        GenreDto genre = service.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("genre", genre);
        return "genre/edit";
    }

    @GetMapping("/genres/addnew")
    public String addnewPage(Model model) {
        GenreDto genre = new GenreDto();
        model.addAttribute("genre", genre);
        return "genre/addnew";
    }

    @PostMapping("/genres/edit")
    public String saveGenre(GenreDto genre) {
        service.update(genre.getId(), genre.getName());
        return "redirect:/genres";
    }

    @PostMapping("/genres")
    public String insertGenre(GenreDto genre) {
        service.insert(genre.getName());
        return "redirect:/genres";
    }
}
