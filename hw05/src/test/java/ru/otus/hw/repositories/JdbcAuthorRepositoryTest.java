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
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами")
@JdbcTest
@Import({JdbcAuthorRepository.class})
public class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository repo;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("Проверяет вывод всех авторов")
    @Test
    public void findAllTest() {
        var authorsGotFromBd = repo.findAll();
        var authorsActual = dbAuthors;
        Assertions.assertThat(authorsGotFromBd).containsExactlyElementsOf(authorsActual);
        authorsGotFromBd.forEach(System.out::println);
    }

    @DisplayName("Проверяет поиск по идентификатору")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    public void findByIdTest(Author author){
        var findAuthor = repo.findById(author.getId());
        Assertions.assertThat(findAuthor)
                .isPresent()
                .get()
                .isEqualTo(author);
    }

    @DisplayName("Проверяет удаление по идентификатору")
    @ParameterizedTest
    @MethodSource("getIntList")
    public void deleteByIdTest(int id){
        var findAuthor = repo.findById(id);
        Assertions.assertThat(findAuthor).isPresent();
        int countBefore = repo.findAll().size();
        repo.deleteById(id);
        findAuthor = repo.findById(id);
        Assertions.assertThat(findAuthor).isNotPresent();
        int countAfter = repo.findAll().size();
        Assertions.assertThat(countAfter).isEqualTo(countBefore-1);
    }

    @DisplayName("Проверяет сохранение нового автора")
    @Test
    public void insertTest(){
        var expectedAuthor = new Author(0L, UUID.randomUUID().toString());
        var savedAuthor = repo.save(expectedAuthor);
        Assertions.assertThat(savedAuthor)
                .isNotNull()
                .matches(author -> author.getId()>0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedAuthor);
        Assertions.assertThat(repo.findById(savedAuthor.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Проверяет сохранение измененного автора")
    @Test
    public void updateTest(){
        long id = 1L;
        var authorInDd = repo.findById(id);
        Assertions.assertThat(authorInDd).isPresent();
        var authorForSave = authorInDd.get();
        authorForSave.setFullName(UUID.randomUUID().toString());
        var savedAuthor = repo.save(authorForSave);
        Assertions.assertThat(savedAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(authorForSave);
        Assertions.assertThat(repo.findById(id))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(authorForSave);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Integer> getIntList(){
        return IntStream.range(1,3).boxed().toList();
    }
}
