package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.CreateRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    private User user;
    private ItemRequest itemRequest;
    private RequestDto requestDto;
    private CreateRequest createRequest;

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

        requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a drill");
        requestDto.setCreated(LocalDateTime.now());

        createRequest = new CreateRequest();
        createRequest.setDescription("Need a drill");
    }

    @Test
    void shouldCreateRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestMapper.toEntity(any(CreateRequest.class), any(User.class)))
                .thenReturn(itemRequest);
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(requestMapper.toDto(any(ItemRequest.class))).thenReturn(requestDto);

        RequestDto result = requestService.create(1L, createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Need a drill");
        verify(requestRepository).save(any(ItemRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForRequest() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.create(999L, createRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void shouldGetUserRequests() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(requestRepository.findByRequestUserIdOrderByCreatedDesc(1L))
                .thenReturn(List.of(itemRequest));
        when(requestMapper.toDto(any(ItemRequest.class))).thenReturn(requestDto);

        var results = requestService.getUserRequests(1L);

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenGetUserRequestsUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> requestService.getUserRequests(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void shouldGetOtherRequests() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(requestRepository.findRequestsByOtherUsers(1L))
                .thenReturn(List.of(itemRequest));
        when(requestMapper.toDto(any(ItemRequest.class))).thenReturn(requestDto);

        var results = requestService.getOtherRequests(1L);

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldGetRequestById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(requestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        when(requestMapper.toDto(any(ItemRequest.class))).thenReturn(requestDto);

        RequestDto result = requestService.getRequestById(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowExceptionWhenRequestNotFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.getRequestById(1L, 999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void shouldThrowExceptionWhenGetRequestUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> requestService.getRequestById(999L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }
}
