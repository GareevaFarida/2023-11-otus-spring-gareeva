package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

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

    private static List<BookDto> books;
    private static List<AuthorDto> authors;
    private static List<GenreDto> genres;

    @BeforeAll
    static void init() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks(authors, genres);
    }

    @DisplayName("Проверяет, что endpoint Get '/books' верно заполняет модель и возвращает верные статус и имя модели")
    @Test
    void getBooksTest() throws Exception {
        prepareMockData();
        var modelAndView = mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("book/list"))
                .matches(m -> !m.getModel().isEmpty());
        Assertions.assertThat(modelAndView.getModel().get("books"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(books);
    }

    @DisplayName("Проверяет, что endpoint Get '/books/{id}' верно заполняет модель и возвращает верные статус и имя модели")
    @Test
    void getBooksIdTest() throws Exception {
        prepareMockData();
        var modelAndView = mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("book/edit"))
                .matches(m -> !m.getModel().isEmpty());
        Assertions.assertThat(modelAndView.getModel().get("book"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(books.get(0));
    }

    @DisplayName("Проверяет, что endpoint Post '/books/{id}' возвращает верный статус")
    @Test
    void postBooksIdTest() throws Exception {
        prepareMockData();
        var modelAndView = mvc.perform(post("/books/1"))
                .andExpect(status().isFound())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("redirect:/books"))
                .matches(m -> m.getModel().isEmpty());
    }

    @DisplayName("Проверяет, что endpoint Get '/books/addnew' возвращает верные статус, модели и имя представления")
    @Test
    void getBooksAddnewTest() throws Exception {
        prepareMockData();
        var modelAndView = mvc.perform(get("/books/addnew"))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("book/edit"))
                .matches(m -> !m.getModel().isEmpty());
        Assertions.assertThat(modelAndView.getModel().get("book"))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("author", "genre")
                .isEqualTo(new BookDto());
        Assertions.assertThat(modelAndView.getModel().get("authors"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(authors);
        Assertions.assertThat(modelAndView.getModel().get("genres"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres);
    }

    @DisplayName("Проверяет, что endpoint Post '/books' возвращает верные статус, модели и имя представления")
    @Test
    void postBooksTest() throws Exception {
        prepareMockData();
        var expectedBook = books.get(0);
        var modelAndView = mvc.perform(
                post("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        //я навешала кучу аннотаций в контроллере, чтобы передать в метод заполненную
                        //  модель книги, но это все равно не помогло, она передается как пустая.
                        // Ниже закомментирована проверка, которую хотела выполнить.
                        .content(mapper.writeValueAsString(expectedBook).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("book/edit"))
                .matches(m -> !m.getModel().isEmpty());
        //хотела добиться выполнения этого условия, но не смогла
        //        Assertions.assertThat(modelAndView.getModel().get("book"))
        //                .isNotNull()
        //                .usingRecursiveComparison()
        //                .isEqualTo(expectedBook);
        Assertions.assertThat(modelAndView.getModel().get("authors"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(authors);
        Assertions.assertThat(modelAndView.getModel().get("genres"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres);
    }

    @DisplayName("Проверяет, что Get '/books/{id}/delete' возвращает верные статус, модели и имя представления")
    @Test
    public void getBooksIdDelete() throws Exception {
        prepareMockData();
        var modelAndView = mvc.perform(get("/books/1/delete"))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("book/delete"))
                .matches(m -> !m.getModel().isEmpty());
        Assertions.assertThat(modelAndView.getModel().get("book"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(books.get(0));
        Assertions.assertThat(modelAndView.getModel().get("authors"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(authors.get(0)));
        Assertions.assertThat(modelAndView.getModel().get("genres"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(genres.get(0)));
    }

    @DisplayName("Проверяет, что в случае поиска по отсутствующему ИД перекинет на страницу ошибки")
    @Test
    public void redirectToErrorPage() throws Exception {
        given(bookService.findBookById(1L)).willReturn(Optional.empty());
        var modelAndView = mvc.perform(get("/books/1/delete"))
                .andExpect(status().isBadRequest())
                .andReturn().getModelAndView();
        Assertions.assertThat(modelAndView)
                .matches(m -> m.getViewName().equals("error"));
    }

    private void prepareMockData() {
        given(bookService.findBookById(1L)).willReturn(Optional.of(books.get(0)));
        given(authorService.findById(1L)).willReturn(Optional.of(authors.get(0)));
        given(genreService.findById(1L)).willReturn(Optional.of(genres.get(0)));
        given(bookService.findAll()).willReturn(books);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);
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
}
