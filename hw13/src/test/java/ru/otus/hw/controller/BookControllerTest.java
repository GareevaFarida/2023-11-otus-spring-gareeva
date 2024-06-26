package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static List<BookDto> books;
    private static List<AuthorDto> authors;
    private static List<GenreDto> genres;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @BeforeAll
    static void init() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks(authors, genres);
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto(id, "Genre" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id, "Title1" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    @BeforeEach
    private void prepareMockData() {
        given(bookService.findBookById(1L)).willReturn(Optional.of(books.get(0)));
        given(authorService.findById(1L)).willReturn(Optional.of(authors.get(0)));
        given(genreService.findById(1L)).willReturn(Optional.of(genres.get(0)));
        given(bookService.findAll()).willReturn(books);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);
    }

    @DisplayName("Проверяет, что endpoint Get '/books' верно заполняет модель и возвращает верные статус и имя модели")
    @WithMockUser
    @Test
    void getBooksTest() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"))
                .andExpect(model().attribute("books", books))
        ;
    }

    @DisplayName("Проверяет, что endpoint Get '/books/{id}' верно заполняет модель и возвращает верные статус и имя модели")
    @WithMockUser
    @Test
    void getBooksIdTest() throws Exception {
        mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attribute("book", books.get(0)));
    }

    @DisplayName("Проверяет, что endpoint Post '/books/{id}' перенеправляет на другой endpoint")
    @WithMockUser
    @Test
    void postBooksIdTest() throws Exception {
        mvc.perform(post("/books/1").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Проверяет, что endpoint Get '/books/addnew' возвращает верные статус, модели и имя представления")
    @WithMockUser
    @Test
    void getBooksAddnewTest() throws Exception {
        mvc.perform(get("/books/addnew"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName("Проверяет, что endpoint Post '/books' возвращает верные статус, модели и имя представления")
    @WithMockUser
    @Test
    void postBooksTest() throws Exception {
        mvc.perform(
                post("/books").with(csrf())
                        .content("title=Title&author.id=1&genre.id=1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"))
        ;
    }

    @DisplayName("Проверяет, что Get '/books/{id}/delete' возвращает верные статус, модели и имя представления")
    @WithMockUser
    @Test
    public void getBooksIdDelete() throws Exception {
        mvc.perform(get("/books/1/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/delete"))
                .andExpect(model().attribute("authors", Optional.of(authors.get(0))))
                .andExpect(model().attribute("genres", Optional.of(genres.get(0))));
    }

    @DisplayName("Проверяет, что в случае поиска по отсутствующему ИД перекинет на страницу ошибки")
    @WithMockUser
    @Test
    public void redirectToErrorPage() throws Exception {
        given(bookService.findBookById(1L)).willReturn(Optional.empty());
        mvc.perform(get("/books/1/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }
}
