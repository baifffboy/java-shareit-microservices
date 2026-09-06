package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("Отправлен запрос на создание пользователя с name: {}", createUserRequest.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(createUserRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("Отправлен запрос на получение пользователя с id: {}", id);
        return ResponseEntity
                .ok()
                .body(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Отправлен запрос на получение всех пользователей");
        return ResponseEntity
                .ok()
                .body(userService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("Отправлен запрос на обновление пользователя с id: {}", id);
        return ResponseEntity
                .ok()
                .body(userService.update(updateUserRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Отправлен запрос на удаление пользователя с id: {}", id);
        userService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
