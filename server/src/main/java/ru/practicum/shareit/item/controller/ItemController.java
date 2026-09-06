package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody CreateItemRequest createItemRequest) {
        log.info("Отправлен запрос на создание вещи пользователем с id: {}", userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.create(userId, createItemRequest));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody CreateCommentRequest createCommentRequest) throws ValidationException {
        log.info("Добавление комментария к вещи с id: {} от пользователя с id: {}", itemId, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.createComment(userId, itemId, createCommentRequest));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody UpdateItemRequest updateItemRequest) {
        log.info("Отправлен запрос на обновление вещи с id: {} пользователем с id: {}", itemId, userId);
        return ResponseEntity
                .ok()
                .body(itemService.update(userId, updateItemRequest, itemId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OwnerItemDto> getItem(
            @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
            @PathVariable Long itemId) {
        log.info("Отправлен запрос на получение вещи с id: {} от пользователя с id: {}", itemId, userId);
        return ResponseEntity
                .ok()
                .body(itemService.findById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<OwnerItemDto>> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Отправлен запрос на получение всех вещей пользователя с id: {}", userId);
        return ResponseEntity
                .ok()
                .body(itemService.findByUserId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String text) {
        log.info("Отправлен запрос на поиск вещей по тексту: {}", text);
        return ResponseEntity
                .ok()
                .body(itemService.search(text));
    }
}
