package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {
    BookingDto toDto(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "WAITING")
    @Mapping(source = "item", target = "item")
    @Mapping(source = "user", target = "booker")
    Booking toBookingCreate(CreateBookingRequest createBookingRequest, Item item, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "start", ignore = true)
    @Mapping(target = "end", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    Booking toBookingUpdate(UpdateBookingRequest updateBookingRequest, @MappingTarget Booking existBooking);

    ItemShortDto mapItemToShort(Item item);

    UserShortDto mapUserToShort(User user);
}
