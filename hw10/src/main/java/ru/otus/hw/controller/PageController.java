package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/library")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/")
    public String listPersonsPage() {
        return "book/list";
    }

    @GetMapping("/books/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "book/edit";
    }

    @GetMapping("/books/addnew")
    public String addnewPage(Model model) {
        model.addAttribute("id", 0);
        return "book/edit";
    }

    @GetMapping("/books/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "book/delete";
    }
}
