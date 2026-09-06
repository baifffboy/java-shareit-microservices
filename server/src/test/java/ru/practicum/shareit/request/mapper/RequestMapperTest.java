package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.CreateRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMapperTest {

    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestUser(user);
    }

    @Test
    void shouldMapItemRequestToDto() {
        RequestDto dto = requestMapper.toDto(itemRequest);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Need a drill");
        assertThat(dto.getCreated()).isNotNull();
    }

    @Test
    void shouldMapCreateRequestToEntity() {
        CreateRequest request = new CreateRequest();
        request.setDescription("Need a hammer");

        ItemRequest entity = requestMapper.toEntity(request, user);

        assertThat(entity).isNotNull();
        assertThat(entity.getDescription()).isEqualTo("Need a hammer");
        assertThat(entity.getRequestUser()).isEqualTo(user);
        assertThat(entity.getCreated()).isNotNull();
        assertThat(entity.getId()).isNull();
    }

    @Test
    void shouldHandleNullItemRequest() {
        RequestDto dto = requestMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void shouldHandleNullCreateRequest() {
        ItemRequest entity = requestMapper.toEntity(null, user);
        assertThat(entity).isNotNull();
        assertThat(entity.getRequestUser()).isEqualTo(user);
        assertThat(entity.getCreated()).isNotNull();
    }

    @Test
    void shouldHandleNullUser() {
        CreateRequest request = new CreateRequest();
        request.setDescription("Need a drill");

        ItemRequest entity = requestMapper.toEntity(request, null);
        assertThat(entity).isNotNull();
        assertThat(entity.getDescription()).isEqualTo("Need a drill");
        assertThat(entity.getRequestUser()).isNull();
    }
}
