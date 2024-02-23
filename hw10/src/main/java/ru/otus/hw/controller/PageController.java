package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/library")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/")
    public String listPersonsPage(Model model) {
        return "book/list";
    }

    @GetMapping("/books/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("id", id);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @GetMapping("/books/addnew")
    public String addnewPage(Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("id", 0);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @GetMapping("/books/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "book/delete";
    }
}
