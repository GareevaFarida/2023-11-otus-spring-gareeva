package ru.otus.hw.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.configuration.SecurityConfiguration;
import ru.otus.hw.security.services.CustomUserDetailsService;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @AllArgsConstructor
    private static class AuthenticationParams {
        private final String username;
        private final String password;
        private final String url;
        private final HttpStatus httpStatus;/*так и не придумала, как его подсунуть в AndExpect(status())*/
    }

    @BeforeEach
    private void prepareMockData() {
        given(customUserDetailsService.loadUserByUsername("user1"))
                .willReturn(new User("user1", "password1", Collections.emptyList()));
    }

    @DisplayName("Проверка доступности ресурсов под разными пользователями")
    @ParameterizedTest
    @MethodSource("getParams")
    public void authentificationTest(AuthenticationParams params) throws Exception {
        mockMvc.perform(get(params.url)
                .with(user(params.username).password(params.password))
        ).andExpect(status().isOk());
    }

    public static List<AuthenticationParams> getParams() {
        return Arrays.asList(
                new AuthenticationParams("user2222",/*почему-то success даже с таким пользователем*/
                        "password1",
                        "/books", HttpStatus.OK)
        );
    }
}
