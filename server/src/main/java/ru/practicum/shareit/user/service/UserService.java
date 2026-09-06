package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(CreateUserRequest createUserRequest);

    UserDto findById(Long id);

    List<UserDto> findAll();

    UserDto update(UpdateUserRequest updateUserRequest, Long id);

    void delete(Long id);
}
