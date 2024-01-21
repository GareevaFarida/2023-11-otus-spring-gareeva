package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
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
    public Genre save(Genre genre) {
        if (genre.getId() == 0L) {
            em.persist(genre);
            return genre;
        }
        return em.merge(genre);
    }

    private String getStringError(long id) {
        return "No one genre with id = %d was found".formatted(id);
    }

}
