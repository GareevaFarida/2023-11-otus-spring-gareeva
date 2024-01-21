package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
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

    @Override
    public Author save(Author author) {
        if (author.getId() == 0L) {
            em.persist(author);
            return author;
        }
        return em.merge(author);
    }

    private String getStringError(long id) {
        return "No one author with id = %d was found".formatted(id);
    }

}
