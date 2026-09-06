package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentRequest;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @Valid @RequestBody CreateItemRequest request) {
        log.info("Creating item by user {}", userId);
        return itemClient.createItem(userId, request);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long itemId,
            @Valid @RequestBody UpdateItemRequest request) {
        log.info("Updating item {} by user {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, request);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @PathVariable @Positive Long itemId) {
        log.info("Get item {}", itemId);
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Get user items for user {}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam String text) {
        log.info("Search items by text: {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long itemId,
            @Valid @RequestBody CreateCommentRequest request) {
        log.info("Creating comment for item {} by user {}", itemId, userId);
        return itemClient.createComment(userId, itemId, request);
    }
}
