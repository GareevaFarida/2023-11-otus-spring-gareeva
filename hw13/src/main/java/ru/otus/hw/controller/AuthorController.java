package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @GetMapping("/authors")
    public String listPage(Model model) {
        List<AuthorDto> authors = service.findAll();
        model.addAttribute("authors", authors);
        return "author/list";
    }

    @GetMapping("/authors/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        AuthorDto author = service.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("author", author);
        return "author/edit";
    }

    @GetMapping("/authors/addnew")
    public String addnewPage(Model model) {
        AuthorDto author = new AuthorDto();
        model.addAttribute("author", author);
        return "author/edit";
    }

    @PostMapping("/authors")
    public String saveAuthor(@ModelAttribute("author") AuthorDto author,
                             BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "author/edit";
        }
        service.update(author.getId(), author.getFullName());
        return "redirect:/authors";
    }
}
