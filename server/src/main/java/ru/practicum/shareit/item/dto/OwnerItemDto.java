package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OwnerItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private List<CommentDto> comments;
    private Long countOfRent;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}
