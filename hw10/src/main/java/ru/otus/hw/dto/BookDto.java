package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookDto {

    private long id;

    @NotBlank(message = "Title shouldn't be empty")
    @Size(max = 1000, message = "expected size should be less than 1000 chars")
    private String title;

    @NotNull(message = "Author shouldn't be empty")
    private AuthorDto author;

    @NotNull(message = "Genre shouldn't be empty")
    private GenreDto genre;

}
