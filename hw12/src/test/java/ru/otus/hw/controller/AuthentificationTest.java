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
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.security.configuration.SecurityConfiguration;
import ru.otus.hw.security.services.CustomUserDetailsService;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        given(bookService.findBookById(1)).willReturn(Optional.of(new BookDto(1L, "tittle", new AuthorDto(), new GenreDto())));
    }

    @DisplayName("Проверка доступности ресурсов под аутентифицированными пользователями")
    @ParameterizedTest
    @MethodSource("generateData")
    public void accessResourcesWithAuthentificatedUserTest(String url, String username, String password, int status) throws Exception {
        mockMvc.perform(get(url).with(user(username).password(password)))
                .andExpect(status().is(status));
    }

    @DisplayName("Проверка что без аутентифицированного пользователя перенаправляет на другой адрес")
    @ParameterizedTest
    @MethodSource("generateData")
    public void authentificationTest(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of("/books", "user", "password", 200),
                Arguments.of("/books/addnew", "user", "password", 200),
                Arguments.of("/books/1", "user", "password", 200),
                Arguments.of("/books/1/delete", "user", "password", 200)
        );
    }
}