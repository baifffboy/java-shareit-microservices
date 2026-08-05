package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto create(Long userId, CreateItemRequest createItemRequest) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemMapper.toEntity(createItemRequest, owner);

        if (createItemRequest.getRequestId() != null) {
            ItemRequest request = requestRepository.findById(createItemRequest.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Данного запроса не существует"));
            log.info("Вещь создана в ответ на запрос с id={}", createItemRequest.getRequestId());
            requestRepository.delete(request);
        }

        Item savedItem = itemRepository.save(item);
        log.info("Создана вещь с id: {} для пользователя с id: {}", savedItem.getId(), userId);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CreateCommentRequest createCommentRequest) throws ValidationException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        List<Booking> bookings = bookingRepository.findAllByItem_IdAndStatusAndEndBeforeOrderByEndDesc(
                itemId,
                Status.APPROVED,
                LocalDateTime.now()
        );

        boolean hasBooked = bookings.stream()
                .anyMatch(b -> b.getBooker().getId().equals(userId));

        if (!hasBooked) {
            throw new ValidationException("Пользователь не арендовал эту вещь или бронирование не завершено");
        }

        Comment comment = new Comment();
        comment.setText(createCommentRequest.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public ItemDto update(Long userId, UpdateItemRequest updateItemRequest, Long itemId) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Редактировать вещь может только её владелец");
        }

        itemMapper.updateEntity(existingItem, updateItemRequest);
        Item updatedItem = itemRepository.save(existingItem);
        log.info("Обновлена вещь с id: {}", updatedItem.getId());
        return itemMapper.toDto(updatedItem);
    }

    @Override
    public OwnerItemDto findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + id + " не найдена"));
        return itemMapper.toDtoOwner(item);
    }

    @Override
    public List<OwnerItemDto> findByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> {
                    bookingRepository.findFirstByItem_IdAndStatusAndEndBeforeOrderByEndDesc(
                            item.getId(),
                            Status.APPROVED,
                            LocalDateTime.now()
                    ).ifPresent(lastBooking -> item.setLastBooking(lastBooking.getEnd()));

                    bookingRepository.findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(
                            item.getId(),
                            Status.APPROVED,
                            LocalDateTime.now()
                    ).ifPresent(nextBooking -> item.setNextBooking(nextBooking.getStart()));

                    return itemMapper.toDtoOwner(item);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String lowerCaseText = text.toLowerCase();
        return itemRepository.findAll().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseText) ||
                        item.getDescription().toLowerCase().contains(lowerCaseText))
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}
