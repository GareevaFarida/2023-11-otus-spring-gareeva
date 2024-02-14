package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String showAllBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/books/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        BookDto book = bookService.findBookById(id).orElseThrow(NotFoundException::new);
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @PostMapping("/books/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/books/addnew")
    public String addnewPage(Model model) {
        BookDto book = new BookDto();
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
            method = {RequestMethod.POST},
            value = "/books")
    public String savebook(@RequestBody/*пыталась добиться передачи заполненного тела из теста, но не получилось*/
                           @Valid
                           @ModelAttribute("book") BookDto book,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var author = authorService.findAll();
            var genre = genreService.findAll();
            model.addAttribute("authors", author);
            model.addAttribute("genres", genre);
            return "book/edit";
        }
        bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        BookDto book = bookService.findBookById(id).orElseThrow(NotFoundException::new);
        var authors = authorService.findById(book.getAuthor().getId());
        var genres = genreService.findById(book.getGenre().getId());
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/delete";
    }
}
