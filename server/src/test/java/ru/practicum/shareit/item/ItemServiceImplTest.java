package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private CreateItemRequest createRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setCountOfRent(0L);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemDto.setCountOfRent(0L);

        createRequest = new CreateItemRequest();
        createRequest.setName("Test Item");
        createRequest.setDescription("Test Description");
        createRequest.setAvailable(true);
    }

    @Test
    void shouldCreateItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemMapper.toEntity(any(CreateItemRequest.class), any(User.class))).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(any(Item.class))).thenReturn(itemDto);

        ItemDto result = itemService.create(1L, createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Item");
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForItemCreation() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.create(999L, createRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void shouldCreateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Test User");

        Booking booking = new Booking();
        booking.setBooker(user);

        CreateCommentRequest commentRequest = new CreateCommentRequest();
        commentRequest.setText("Great item!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItem_IdAndStatusAndEndBeforeOrderByEndDesc(
                anyLong(), any(Status.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto result = itemService.createComment(1L, 1L, commentRequest);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo("Great item!");
    }

    @Test
    void shouldThrowExceptionWhenUserHasNotBookedItem() {
        CreateCommentRequest commentRequest = new CreateCommentRequest();
        commentRequest.setText("Great item!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItem_IdAndStatusAndEndBeforeOrderByEndDesc(
                anyLong(), any(Status.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        assertThatThrownBy(() -> itemService.createComment(1L, 1L, commentRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не арендовал");
    }

    @Test
    void shouldUpdateItem() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setName("Updated Item");
        updateRequest.setDescription("Updated Description");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(any(Item.class))).thenReturn(itemDto);

        ItemDto result = itemService.update(1L, updateRequest, 1L);

        assertThat(result).isNotNull();
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        User otherUser = new User();
        otherUser.setId(2L);
        item.setOwner(otherUser);

        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setName("Updated Item");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> itemService.update(1L, updateRequest, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("только её владелец");
    }

    @Test
    void shouldFindItemById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toDtoOwner(any(Item.class))).thenReturn(new OwnerItemDto());

        OwnerItemDto result = itemService.findById(1L, 1L);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldFindItemsByUserId() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of(item));
        when(itemMapper.toDtoOwner(any(Item.class))).thenReturn(new OwnerItemDto());

        List<OwnerItemDto> results = itemService.findByUserId(1L);

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldSearchItems() {
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemMapper.toDto(any(Item.class))).thenReturn(itemDto);

        List<ItemDto> results = itemService.search("Test");

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListForEmptySearch() {
        List<ItemDto> results = itemService.search("");

        assertThat(results).isEmpty();
    }

    @Test
    void shouldReturnEmptyListForNullSearch() {
        List<ItemDto> results = itemService.search(null);

        assertThat(results).isEmpty();
    }

    @Test
    void shouldSearchItemsCaseInsensitive() {
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemMapper.toDto(any(Item.class))).thenReturn(itemDto);

        List<ItemDto> results = itemService.search("test");

        assertThat(results).hasSize(1);
    }
}
