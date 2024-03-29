package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return findByIdAndEntityGraph(id, "book-entity-graph");
    }

    private Optional<Book> findByIdAndEntityGraph(long id, String entityGraphName) {
        EntityGraph<?> entityGraph = em.getEntityGraph(entityGraphName);
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b where b.id=:id",
                Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        query.setParameter("id", id);
        var list = query.getResultList();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<Book> findByIdWithComments(long id) {
        return findByIdAndEntityGraph(id, "book-entity-graph-comments");
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b",
                Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id)
                .ifPresentOrElse(
                        em::remove,
                        () -> {
                            throw new EntityNotFoundException(getStringError(id));
                        }
                );
    }

    private String getStringError(long id) {
        return "No one book with id = %d was found".formatted(id);
    }
}
