package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.security.dto.UserDto;
import ru.otus.hw.security.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> modelMapper.map(user, UserDto.class));
    }
}
