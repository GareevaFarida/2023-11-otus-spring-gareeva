package ru.otus.hw.security.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.security.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
