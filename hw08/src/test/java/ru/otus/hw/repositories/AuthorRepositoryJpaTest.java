//package ru.otus.hw.repositories;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import ru.otus.hw.models.Author;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.IntStream;
//import java.util.stream.LongStream;
//
//@DisplayName("Репозиторий на основе JPA для работы с авторами")
//@DataJpaTest
//public class AuthorRepositoryJpaTest {
//
//    @Autowired
//    private TestEntityManager em;
//
//    @Autowired
//    private AuthorRepository repo;
//
//    @DisplayName("Проверяет вывод всех авторов")
//    @Test
//    public void findAllTest() {
//        List<Author> dbAuthors = getDbAuthors();
//        var authorsGotFromBd = repo.findAll();
//        var authorsActual = dbAuthors;
//        Assertions.assertThat(authorsGotFromBd).containsExactlyElementsOf(authorsActual);
//        authorsGotFromBd.forEach(System.out::println);
//    }
//
//    @DisplayName("Проверяет поиск по идентификатору")
//    @ParameterizedTest
//    @MethodSource("getLongList")
//    public void findByIdTest(Long id) {
//        var expectedAuthor = em.find(Author.class, id);
//        var findAuthor = repo.findById(id);
//        Assertions.assertThat(findAuthor)
//                .isPresent()
//                .get()
//                .isEqualTo(expectedAuthor);
//    }
//
//    @DisplayName("Проверяет удаление по идентификатору")
//    @Test
//    public void deleteByIdTest() {
//        Author newAuthor = new Author(0L, "Новый автор для тестирования");
//        em.persist(newAuthor);
//        Assertions.assertThat(newAuthor.getId()).isNotEqualTo(0L);
//        var findAuthor = em.find(Author.class, newAuthor.getId());
//        Assertions.assertThat(findAuthor).isNotNull();
//
//        repo.deleteById(findAuthor.getId());
//        var deletedAuthor = em.find(Author.class, newAuthor.getId());
//        Assertions.assertThat(deletedAuthor).isNull();
//    }
//
//    @DisplayName("Проверяет сохранение нового автора")
//    @Test
//    public void insertTest() {
//        var expectedAuthor = new Author(0L, UUID.randomUUID().toString());
//        var savedAuthor = repo.save(expectedAuthor);
//        Assertions.assertThat(savedAuthor)
//                .isNotNull()
//                .matches(author -> author.getId() > 0)
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(expectedAuthor);
//        Assertions.assertThat(em.find(Author.class, savedAuthor.getId()))
//                .isNotNull()
//                .isEqualTo(savedAuthor);
//    }
//
//    @DisplayName("Проверяет сохранение измененного автора")
//    @Test
//    public void updateTest() {
//        long id = 1L;
//        var authorInDd = em.find(Author.class, id);
//        Assertions.assertThat(authorInDd).isNotNull();
//        var authorForSave = new Author(authorInDd.getId(), UUID.randomUUID().toString());
//        var savedAuthor = repo.save(authorForSave);
//        Assertions.assertThat(em.find(Author.class, id))
//                .usingRecursiveComparison()
//                .isEqualTo(savedAuthor);
//    }
//
//    private static List<Author> getDbAuthors() {
//        return IntStream.range(1, 16).boxed()
//                .map(id -> new Author(id, "Author_" + id))
//                .toList();
//    }
//
//    private static List<Long> getLongList() {
//        return LongStream.range(1, 16).boxed().toList();
//    }
//}
