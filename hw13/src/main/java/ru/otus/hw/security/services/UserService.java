package ru.otus.hw.security.services;

import ru.otus.hw.security.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> findUserByUsername(String username);
}
