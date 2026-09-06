package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", uses = CommentMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {

    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "nextBooking", source = "nextBooking")
    OwnerItemDto toDtoOwner(Item item);

    ItemDto toDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createItemRequest.name", target = "name")
    @Mapping(source = "createItemRequest.description", target = "description")
    @Mapping(source = "createItemRequest.available", target = "available")
    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "owner", target = "owner")
    @Mapping(target = "countOfRent", constant = "0L")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item toEntity(CreateItemRequest createItemRequest, User owner);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "countOfRent", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    void updateEntity(@MappingTarget Item existingItem, UpdateItemRequest updateItemRequest);
}
