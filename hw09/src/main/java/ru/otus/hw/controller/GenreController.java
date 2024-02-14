package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        return "genre/edit";
    }

    @PostMapping("/genres")
    public String saveGenre(@Valid @ModelAttribute("genre") GenreDto genre, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "genre/edit";
        }
        service.update(genre.getId(), genre.getName());
        return "redirect:/genres";
    }
}
