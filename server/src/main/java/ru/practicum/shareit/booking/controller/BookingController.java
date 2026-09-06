package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(
            @RequestBody CreateBookingRequest createBookingRequest,
            @RequestHeader("X-Sharer-User-Id") Long userId) throws ValidationException {
        log.info("Создан запрос на бронирование - по умолчанию статус WAITING");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.create(createBookingRequest, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> patch(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) throws ValidationException {
        log.info("Значение status у id={} изменено на {}", bookingId, approved);
        return ResponseEntity
                .ok()
                .body(bookingService.patch(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long bookingId) {
        log.info("Возврат бронирования с id={}", bookingId);
        return ResponseEntity
                .ok()
                .body(bookingService.getBookingById(bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookingByLeaseholder(
            @RequestParam(required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка всех бронирований текущего пользователя c id={}", userId);
        return ResponseEntity
                .ok()
                .body(bookingService.getBookingByLeaseholder(userId));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getBookingByOwner(
            @RequestParam(required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка всех бронирований у арендодателя(owner) c id={}", userId);
        return ResponseEntity
                .ok()
                .body(bookingService.getBookingByOwner(userId));
    }
}
