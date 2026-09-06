package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;
    private CreateBookingRequest createRequest;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus(Status.WAITING);

        createRequest = new CreateBookingRequest();
        createRequest.setItemId(1L);
        createRequest.setStart(LocalDateTime.now().plusDays(1));
        createRequest.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        when(bookingService.create(any(CreateBookingRequest.class), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldPatchBooking() throws Exception {
        when(bookingService.patch(anyLong(), any(Boolean.class), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 2L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBooking() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldGetBookingsByLeaseholder() throws Exception {
        when(bookingService.getBookingByLeaseholder(1L))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookingsByOwner() throws Exception {
        when(bookingService.getBookingByOwner(2L))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}
