package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating user: {}", request.getName());
        return userClient.createUser(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable @Positive Long id) {
        log.info("Get user {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Update user {}", id);
        return userClient.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive Long id) {
        log.info("Delete user {}", id);
        return userClient.deleteUser(id);
    }
}
