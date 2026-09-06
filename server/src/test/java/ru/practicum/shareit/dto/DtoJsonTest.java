package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DtoJsonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeUserDto() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        String json = objectMapper.writeValueAsString(dto);
        UserDto result = objectMapper.readValue(json, UserDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldSerializeCommentDto() throws Exception {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Great item!");
        dto.setAuthorName("Test User");
        dto.setCreated(LocalDateTime.now());

        String json = objectMapper.writeValueAsString(dto);
        CommentDto result = objectMapper.readValue(json, CommentDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo("Great item!");
        assertThat(result.getAuthorName()).isEqualTo("Test User");
    }

    @Test
    void shouldSerializeItemDto() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        dto.setAvailable(true);
        dto.setCountOfRent(5L);

        String json = objectMapper.writeValueAsString(dto);
        ItemDto result = objectMapper.readValue(json, ItemDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Item");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.isAvailable()).isTrue();
        assertThat(result.getCountOfRent()).isEqualTo(5L);
    }

    @Test
    void shouldSerializeBookingDto() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        dto.setStatus(Status.WAITING);

        String json = objectMapper.writeValueAsString(dto);
        BookingDto result = objectMapper.readValue(json, BookingDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void shouldSerializeRequestDto() throws Exception {
        RequestDto dto = new RequestDto();
        dto.setId(1L);
        dto.setDescription("Need a drill");
        dto.setCreated(LocalDateTime.now());

        String json = objectMapper.writeValueAsString(dto);
        RequestDto result = objectMapper.readValue(json, RequestDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Need a drill");
    }
}
