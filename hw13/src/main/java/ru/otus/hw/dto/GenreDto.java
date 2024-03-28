package ru.otus.hw.dto;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
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
public class GenreDto {

    private long id;

//    @NotBlank(message = "Genre's name shouldn't be empty")
//    @Size(min = 2, max = 100, message = "expected size should be more than 2 chars")
//    @Size(max = 20, message = "expected size should be less than 20 chars")
    private String name;
}
