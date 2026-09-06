package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
public class UpdateRequest {
    private Long id;
    private ItemDto request;
    private boolean status;
    private UserDto requestUser;
}
