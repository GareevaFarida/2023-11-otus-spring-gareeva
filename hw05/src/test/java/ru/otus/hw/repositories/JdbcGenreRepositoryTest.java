package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import({JdbcGenreRepository.class})
public class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repo;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("Проверяет вывод всех жанров")
    @Test
    public void findAllTest() {
        var genresGotFromBd = repo.findAll();
        var genresActual = dbGenres;
        Assertions.assertThat(genresGotFromBd).containsExactlyElementsOf(genresActual);
        genresGotFromBd.forEach(System.out::println);
    }

    @DisplayName("Проверяет поиск по идентификатору")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    public void findByIdTest(Genre genre) {
        var findGenre = repo.findById(genre.getId());
        Assertions.assertThat(findGenre)
                .isPresent()
                .get()
                .isEqualTo(genre);
    }

    @DisplayName("Проверяет удаление по идентификатору")
    @ParameterizedTest
    @MethodSource("getIntList")
    public void deleteByIdTest(int id) {
        var findGenre = repo.findById(id);
        Assertions.assertThat(findGenre).isPresent();
        int countBefore = repo.findAll().size();
        repo.deleteById(id);
        findGenre = repo.findById(id);
        Assertions.assertThat(findGenre).isNotPresent();
        int countAfter = repo.findAll().size();
        Assertions.assertThat(countAfter).isEqualTo(countBefore - 1);
    }

    @DisplayName("Проверяет сохранение нового жанра")
    @Test
    public void insertTest() {
        var expected = new Genre(0L, UUID.randomUUID().toString());
        var saved = repo.save(expected);
        Assertions.assertThat(saved)
                .isNotNull()
                .matches(genre -> genre.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
        Assertions.assertThat(repo.findById(saved.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @DisplayName("Проверяет сохранение измененного жанра")
    @Test
    public void updateTest() {
        long id = 1L;
        var genreInDd = repo.findById(id);
        Assertions.assertThat(genreInDd).isPresent();
        var genreForSave = genreInDd.get();
        genreForSave.setName(UUID.randomUUID().toString());
        var savedGenre = repo.save(genreForSave);
        Assertions.assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genreForSave);
        Assertions.assertThat(repo.findById(id))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(genreForSave);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Integer> getIntList() {
        return IntStream.range(1, 3).boxed().toList();
    }
}
