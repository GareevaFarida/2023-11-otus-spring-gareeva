package ru.otus.hw.security.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.security.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
