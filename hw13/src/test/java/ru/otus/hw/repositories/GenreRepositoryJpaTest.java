package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@DisplayName("Репозиторий на основе JPA для работы с жанрами")
@DataJpaTest
public class GenreRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository repo;

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Long> getLongList() {
        return LongStream.range(1, 3).boxed().toList();
    }

    @DisplayName("Проверяет вывод всех жанров")
    @Test
    public void findAllTest() {
        var genresGotFromBd = repo.findAll();
        var genresActual = getDbGenres();
        Assertions.assertThat(genresGotFromBd).containsExactlyElementsOf(genresActual);
        genresGotFromBd.forEach(System.out::println);
    }

    @DisplayName("Проверяет поиск по идентификатору")
    @ParameterizedTest
    @MethodSource("getLongList")
    public void findByIdTest(long id) {
        var expectedGenre = em.find(Genre.class, id);
        var findGenre = repo.findById(id);
        Assertions.assertThat(findGenre)
                .isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("Проверяет удаление по идентификатору")
    @Test
    public void deleteByIdTest() {
        var newGenre = new Genre(0L, "Новый жанр для тестирования");
        em.persist(newGenre);
        Assertions.assertThat(newGenre.getId()).isNotEqualTo(0L);
        var findGenre = em.find(Genre.class, newGenre.getId());
        Assertions.assertThat(findGenre).isNotNull();

        repo.deleteById(findGenre.getId());
        var deletedGenre = em.find(Genre.class, newGenre.getId());
        Assertions.assertThat(deletedGenre).isNull();
    }

    @DisplayName("Проверяет сохранение нового жанра")
    @Test
    public void insertTest() {
        var expectedGenre = new Genre(0L, UUID.randomUUID().toString());
        var savedGenre = repo.save(expectedGenre);
        Assertions.assertThat(savedGenre)
                .isNotNull()
                .matches(genre -> genre.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedGenre);
        Assertions.assertThat(em.find(Genre.class, savedGenre.getId()))
                .isNotNull()
                .isEqualTo(savedGenre);
    }

    @DisplayName("Проверяет сохранение измененного жанра")
    @Test
    public void updateTest() {
        long id = 1L;
        var genreInDd = em.find(Genre.class, id);
        Assertions.assertThat(genreInDd).isNotNull();
        var genreForSave = new Genre(genreInDd.getId(), UUID.randomUUID().toString());
        var savedGenre = repo.save(genreForSave);
        Assertions.assertThat(em.find(Genre.class, id))
                .usingRecursiveComparison()
                .isEqualTo(savedGenre);
    }
}
