package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.security.configuration.SecurityConfiguration;
import ru.otus.hw.security.services.CustomUserDetailsService;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({SecurityConfiguration.class})
public class AuthentificationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    private void prepareMockData() {
        var author = new AuthorDto(1L, "author");
        var genre = new GenreDto(1L, "genre");
        var book = new BookDto(1L, "title", author, genre);
        given(bookService.findBookById(1)).willReturn(Optional.of(book));
        given(authorService.findAll()).willReturn(Collections.singletonList(author));
        given(genreService.findAll()).willReturn(Collections.singletonList(genre));
        given(bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId()))
                .willReturn(book);
    }

    @DisplayName("Проверка доступности ресурсов под аутентифицированными пользователями с заданными ролями")
    @ParameterizedTest
    @MethodSource("generateData")
    public void accessResourcesWithAuthentificatedUserTest(HttpMethod httpMethod,
                                                           String url,
                                                           String username,
                                                           String password,
                                                           String role,
                                                           int status) throws Exception {
        mockMvc.perform(request(httpMethod, url)
                .with(user(username).password(password).roles(role))
        ).andExpect(status().is(status));
    }

    @DisplayName("Проверка что без аутентифицированного пользователя перенаправляет на другой адрес")
    @ParameterizedTest
    @MethodSource("generateData")
    public void authentificationTest(HttpMethod httpMethod, String url, String user, String password, String role, int status, String content) throws Exception {
        mockMvc.perform(request(httpMethod, url).content(content).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    private static Stream<Arguments> generateData() {
        var bookString = "title=Title&author.id=1&genre.id=1";
        return Stream.of(
                Arguments.of(HttpMethod.GET, "/books", "user", "password", "MANAGER", 200, ""),
                Arguments.of(HttpMethod.GET, "/books", "user", "password", "CUSTOMER", 200, ""),
//                Arguments.of(HttpMethod.POST, "/books", "user1", "password1", "MANAGER", 200, bookString),
//                Arguments.of(HttpMethod.POST, "/books", "user", "password", "CUSTOMER", 403, bookString),
                Arguments.of(HttpMethod.GET, "/books/addnew", "user", "password", "MANAGER", 200, ""),
                Arguments.of(HttpMethod.GET, "/books/addnew", "user", "password", "CUSTOMER", 403, ""),
                Arguments.of(HttpMethod.GET, "/books/1", "user", "password", "MANAGER", 200, ""),
                Arguments.of(HttpMethod.GET, "/books/1", "user", "password", "CUSTOMER", 403, ""),
//                Arguments.of(HttpMethod.POST, "/books/1", "user", "password", "MANAGER", 200, bookString),
//                Arguments.of(HttpMethod.POST, "/books/1", "user", "password", "CUSTOMER", 403, bookString),
                Arguments.of(HttpMethod.GET, "/books/1/delete", "user", "password", "MANAGER", 200, ""),
                Arguments.of(HttpMethod.GET, "/books/1/delete", "user", "password", "CUSTOMER", 403, "")
        );
    }

}