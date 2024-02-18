package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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

    //todo перетащить в другой контроллер, который просто @Controller
    @GetMapping("/")
    public String listPersonsPage(Model model) {
        //model.addAttribute("keywords", "list users in Omsk, omsk, list users, list users free");
        return "book/list";
    }

    @ResponseBody
    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> showAllBooks(Model model) {
        return ResponseEntity.ok(bookService.findAll());
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

    @ResponseBody
    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok("");
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

    @ResponseBody
    @PostMapping("/books")
    public ResponseEntity<BookDto> savebook(@Valid
                                            @RequestBody
                                            BookDto book,
                                            BindingResult bindingResult,
                                            Model model) {
        if (bindingResult.hasErrors()) {
            var author = authorService.findAll();
            var genre = genreService.findAll();
            model.addAttribute("authors", author);
            model.addAttribute("genres", genre);
            return ResponseEntity.badRequest().body(book);
        }
        var savedBook = bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return ResponseEntity.ok(savedBook);
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
