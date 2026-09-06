package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private BookItemRequestDto validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
    }

    @Test
    void shouldCreateBookingWithValidData() throws Exception {
        when(bookingClient.bookItem(anyLong(), any(BookItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenStartDateInPast() throws Exception {
        BookItemRequestDto invalidRequest = new BookItemRequestDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEndDateInPast() throws Exception {
        BookItemRequestDto invalidRequest = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1)
        );

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenItemIdIsNull() throws Exception {
        BookItemRequestDto invalidRequest = new BookItemRequestDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBookings() throws Exception {
        when(bookingClient.getBookings(anyLong(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        when(bookingClient.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookingsByOwner() throws Exception {
        when(bookingClient.getBookingsByOwner(anyLong(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    /**
     * Исправленный тест - теперь проверяем, что при invalid state возвращается 400
     * В контроллере используется @Validated, который обрабатывает IllegalArgumentException
     * через GlobalExceptionHandler
     */
    @Test
    void shouldReturnBadRequestWhenInvalidState() throws Exception {
        // Теперь проверяем, что запрос с невалидным state возвращает 400
        // IllegalArgumentException должен быть перехвачен GlobalExceptionHandler
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "INVALID_STATE"))
                .andExpect(status().isBadRequest());
    }
}
