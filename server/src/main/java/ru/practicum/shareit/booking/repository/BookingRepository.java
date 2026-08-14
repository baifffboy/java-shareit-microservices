package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findByItem_Owner_IdOrderByStartDesc(Long userId);

    Optional<Booking> findFirstByItem_IdAndStatusAndEndBeforeOrderByEndDesc(
            Long itemId,
            Status status,
            LocalDateTime now
    );

    List<Booking> findAllByItem_IdAndStatusAndEndBeforeOrderByEndDesc(
            Long itemId,
            Status status,
            LocalDateTime now
    );

    List<Booking> findAllByItem_IdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId,
            Status status,
            LocalDateTime now
    );

    Optional<Booking> findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId,
            Status status,
            LocalDateTime now
    );
}
