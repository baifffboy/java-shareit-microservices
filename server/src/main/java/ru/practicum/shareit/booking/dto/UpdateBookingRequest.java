package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UpdateBookingRequest {
    private Long id;
    private Status status;
}
