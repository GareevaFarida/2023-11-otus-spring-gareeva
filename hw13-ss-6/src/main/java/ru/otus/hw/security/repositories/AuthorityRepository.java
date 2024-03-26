package ru.otus.hw.security.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.security.models.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
