//package ru.otus.hw.repositories;
//
//import lombok.val;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import ru.otus.hw.models.Author;
//import ru.otus.hw.models.Book;
//import ru.otus.hw.models.Comment;
//import ru.otus.hw.models.Genre;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.LongStream;
//
//import static java.util.Objects.nonNull;
//
//@DisplayName("Репозиторий на основе JPA для работы с комментариями")
//@DataJpaTest
//public class CommentRepositoryJpaTest {
//
//    @Autowired
//    private TestEntityManager em;
//
//    @Autowired
//    private CommentRepository repo;
//
//    @DisplayName("Проверяет вывод всех комментариев одной книги")
//    @Test
//    public void findAllByBookIdTest() {
//        var author = new Author(0L, "Новый автор");
//        var genre = new Genre(0L, "Новый жанр");
//        var book = new Book(0L, "new book", author, genre, new ArrayList<>());
//        var comments = Arrays.asList(
//                new Comment(0L, "Отличная книга!", book),
//                new Comment(0L, "Книга как книга, подумаешь...", book));
//        book.getComments().addAll(comments);
//        em.persist(book);
//        Assertions.assertThat(book).matches(b -> b.getId() > 0L)
//                .matches(b -> nonNull(b.getGenre()))
//                .matches(b -> nonNull(b.getAuthor()))
//                .matches(b -> b.getComments().size() == 2);
//        val findComments = repo.findAllByBookId(book.getId());
//        Assertions.assertThat(findComments)
//                .usingRecursiveComparison()
//                .ignoringFields("book", "id")
//                .isEqualTo(comments);
//    }
//
//    @DisplayName("Проверяет поиск по идентификатору")
//    @ParameterizedTest
//    @MethodSource("getLongList")
//    public void findByIdTest(long id) {
//        var expectedObj = em.find(Comment.class, id);
//        var findObj = repo.findById(id);
//        Assertions.assertThat(findObj)
//                .isPresent()
//                .get()
//                .isEqualTo(expectedObj);
//    }
//
//    @DisplayName("Проверяет удаление по идентификатору")
//    @Test
//    public void deleteByIdTest() {
//        var book = em.find(Book.class, 1L);
//        Assertions.assertThat(book).isNotNull();
//        var newComment = new Comment(0L, "Новый комментарий для тестирования", book);
//        em.persist(newComment);
//        Assertions.assertThat(newComment.getId()).isNotEqualTo(0L);
//        repo.deleteById(newComment.getId());
//        var deletedComment = em.find(Comment.class, newComment.getId());
//        Assertions.assertThat(deletedComment).isNull();
//    }
//
//    @DisplayName("Проверяет сохранение нового комментария")
//    @Test
//    public void insertTest() {
//        var book = em.find(Book.class, 1L);
//        Assertions.assertThat(book).isNotNull();
//        var newComment = new Comment(0L, "Новый комментарий для тестирования", book);
//        var savedComment = repo.save(newComment);
//        Assertions.assertThat(savedComment)
//                .isNotNull()
//                .matches(comment -> comment.getId() > 0)
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(newComment);
//        Assertions.assertThat(em.find(Comment.class, savedComment.getId()))
//                .isNotNull()
//                .isEqualTo(savedComment);
//
//    }
//
//    @DisplayName("Проверяет сохранение измененного комментария")
//    @Test
//    public void updateTest() {
//        long id = 1L;
//        var commentInDd = em.find(Comment.class, id);
//        Assertions.assertThat(commentInDd).isNotNull();
//        var newCommentText = UUID.randomUUID().toString();
//        var commentForSave = new Comment(commentInDd.getId(), newCommentText, commentInDd.getBook());
//        var savedComment = repo.save(commentForSave);
//        Assertions.assertThat(em.find(Comment.class, id))
//                .usingRecursiveComparison()
//                .isEqualTo(savedComment);
//    }
//
//    private static List<Long> getLongList() {
//        return LongStream.range(1, 10).boxed().toList();
//    }
//}
