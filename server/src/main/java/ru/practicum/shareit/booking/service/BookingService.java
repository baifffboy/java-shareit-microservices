package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface BookingService {
    BookingDto create(CreateBookingRequest createBookingRequest, Long userId) throws ValidationException;

    BookingDto patch(Long bookingId, Boolean approved, Long userId) throws ValidationException;

    BookingDto getBookingById(Long bookingId);

    List<BookingDto> getBookingByLeaseholder(Long userId);

    List<BookingDto> getBookingByOwner(Long userId);
}
