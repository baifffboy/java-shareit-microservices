package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, CreateItemRequest createItemRequest);

    CommentDto createComment(Long userId, Long itemId, CreateCommentRequest createItemRequest) throws ValidationException;

    ItemDto update(Long userId, UpdateItemRequest updateItemRequest, Long itemId);

    OwnerItemDto findById(Long id, Long userId);

    List<OwnerItemDto> findByUserId(Long userId);

    List<ItemDto> search(String text);
}
