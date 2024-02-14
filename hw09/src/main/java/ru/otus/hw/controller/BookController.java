package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;
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
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/books/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        BookWithCommentsDto book = bookService.findByIdWithComments(id).orElseThrow(NotFoundException::new);
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @PostMapping("/books/{id}")
    public String deleteBook(@PathVariable("id") long id, Model model) {
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

    @PostMapping("/books")
    public String savebook(BookDto book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/books";
    }
}
