package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    private User user;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        owner = new User();
        owner.setId(2L);
        owner.setName("Test Owner");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);
    }

    @Test
    void shouldMapBookingToDto() {
        BookingDto dto = bookingMapper.toDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isNotNull();
        assertThat(dto.getEnd()).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(Status.WAITING);
        assertThat(dto.getItem()).isNotNull();
        assertThat(dto.getItem().getId()).isEqualTo(1L);
        assertThat(dto.getBooker()).isNotNull();
        assertThat(dto.getBooker().getId()).isEqualTo(1L);
    }

    @Test
    void shouldMapCreateRequestToBooking() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        Booking created = bookingMapper.toBookingCreate(request, item, user);

        assertThat(created).isNotNull();
        assertThat(created.getStart()).isNotNull();
        assertThat(created.getEnd()).isNotNull();
        assertThat(created.getStatus()).isEqualTo(Status.WAITING);
        assertThat(created.getItem()).isEqualTo(item);
        assertThat(created.getBooker()).isEqualTo(user);
        assertThat(created.getId()).isNull();
    }

    @Test
    void shouldUpdateBookingFromRequest() {
        UpdateBookingRequest request = new UpdateBookingRequest();
        request.setId(1L);
        request.setStatus(Status.APPROVED);

        Booking updated = bookingMapper.toBookingUpdate(request, booking);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(updated.getStart()).isEqualTo(booking.getStart());
        assertThat(updated.getEnd()).isEqualTo(booking.getEnd());
    }

    @Test
    void shouldHandleNullUpdateRequest() {
        Booking updated = bookingMapper.toBookingUpdate(null, booking);
        assertThat(updated).isEqualTo(booking);
    }

    @Test
    void shouldMapItemToShort() {
        var shortDto = bookingMapper.mapItemToShort(item);

        assertThat(shortDto).isNotNull();
        assertThat(shortDto.getId()).isEqualTo(1L);
        assertThat(shortDto.getName()).isEqualTo("Test Item");
        assertThat(shortDto.getDescription()).isEqualTo("Test Description");
    }

    @Test
    void shouldMapUserToShort() {
        var shortDto = bookingMapper.mapUserToShort(user);

        assertThat(shortDto).isNotNull();
        assertThat(shortDto.getId()).isEqualTo(1L);
        assertThat(shortDto.getName()).isEqualTo("Test User");
    }

    @Test
    void shouldHandleNullBooking() {
        BookingDto dto = bookingMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void shouldHandleNullItemInMapping() {
        var shortDto = bookingMapper.mapItemToShort(null);
        assertThat(shortDto).isNull();
    }
}