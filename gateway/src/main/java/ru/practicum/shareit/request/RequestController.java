package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateRequest;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @Valid @RequestBody CreateRequest request) {
        log.info("Creating request by user {}", userId);
        return requestClient.createRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Get user requests for user {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Get other requests for user {}", userId);
        return requestClient.getOtherRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long requestId) {
        log.info("Get request {} for user {}", requestId, userId);
        return requestClient.getRequest(userId, requestId);
    }
}
