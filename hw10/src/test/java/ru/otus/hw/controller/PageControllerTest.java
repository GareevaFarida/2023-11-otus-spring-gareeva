package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({PageController.class})
public class PageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("Проверяет, что endpoint Get '/books/{id}' возвращает верные статус и имя представления")
    @Test
    void getBooksIdTest() throws Exception {
        mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"));
    }

    @DisplayName("Проверяет, что endpoint Get '/books/addnew' возвращает верные статус и имя представления")
    @Test
    void getBooksAddnewTest() throws Exception {
        mvc.perform(get("/books/addnew"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"));
    }

    @DisplayName("Проверяет, что Get '/books/{id}/delete' возвращает верные статус и имя представления")
    @Test
    public void getBooksIdDelete() throws Exception {
        mvc.perform(get("/books/1/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/delete"));
    }
}
