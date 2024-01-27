package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

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

    private String getStringError(long id) {
        return "No one book with id = %d was found".formatted(id);
    }
}
