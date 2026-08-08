package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        UserDto dto = userMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test User");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldMapCreateRequestToEntity() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");

        User user = userMapper.toEntity(request);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getId()).isNull();
    }

    @Test
    void shouldUpdateEntityFromRequest() {
        User user = new User();
        user.setId(1L);
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("New Name");
        request.setEmail("new@example.com");

        userMapper.updateEntity(user, request);

        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateEntityPartially() {
        User user = new User();
        user.setId(1L);
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("New Name");
        // email не обновляем

        userMapper.updateEntity(user, request);

        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getEmail()).isEqualTo("old@example.com");
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    void shouldHandleNullRequest() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        userMapper.updateEntity(user, null);

        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getId()).isEqualTo(1L);
    }
}
