package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(CreateUserRequest createUserRequest) {
        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(createUserRequest.getEmail()));
        if (emailExists) {
            throw new ConflictException("Пользователь с почтой " + createUserRequest.getEmail() + " уже существует");
        }
        User user = userMapper.toEntity(createUserRequest);
        User savedUser = userRepository.save(user);
        log.info("Создан пользователь с id: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UpdateUserRequest updateUserRequest, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));

        if (updateUserRequest.getEmail() != null) {
            boolean emailExists = userRepository.findAll().stream()
                    .anyMatch(user -> !user.getId().equals(id)
                            && user.getEmail().equals(updateUserRequest.getEmail()));
            if (emailExists) {
                throw new ConflictException("Пользователь с почтой " + updateUserRequest.getEmail() + " уже существует");
            }
        }

        userMapper.updateEntity(existingUser, updateUserRequest);
        User updatedUser = userRepository.save(existingUser);
        log.info("Обновлен пользователь с id: {}", updatedUser.getId());
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
        log.info("Удален пользователь с id: {}", id);
    }
}
