package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
public class UpdateRequest {
    private Long id;
    private Item request;
    private boolean status;
    private User requestUser;
}
