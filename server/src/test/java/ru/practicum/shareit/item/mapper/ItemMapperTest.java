package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private CommentMapper commentMapper;

    private ItemMapper itemMapper;
    private User owner;
    private Item item;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        ItemMapperImpl mapperImpl = new ItemMapperImpl();
        try {
            var field = ItemMapperImpl.class.getDeclaredField("commentMapper");
            field.setAccessible(true);
            field.set(mapperImpl, commentMapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject commentMapper", e);
        }
        itemMapper = mapperImpl;

        owner = new User();
        owner.setId(1L);
        owner.setName("Test Owner");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(owner);
        comment.setCreated(LocalDateTime.now());

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Test Owner");
        commentDto.setCreated(comment.getCreated());

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setCountOfRent(5L);
        item.setComments(List.of(comment));
        item.setLastBooking(LocalDateTime.now().minusDays(1));
        item.setNextBooking(LocalDateTime.now().plusDays(1));

        lenient().when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);
    }

    @Test
    void shouldMapItemToDto() {
        ItemDto dto = itemMapper.toDto(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Item");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.isAvailable()).isTrue();
        assertThat(dto.getCountOfRent()).isEqualTo(5L);
        assertThat(dto.getComments()).hasSize(1);
        assertThat(dto.getComments().get(0).getText()).isEqualTo("Great item!");
        assertThat(dto.getComments().get(0).getAuthorName()).isEqualTo("Test Owner");
    }

    @Test
    void shouldMapItemToOwnerDto() {
        OwnerItemDto dto = itemMapper.toDtoOwner(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Item");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.isAvailable()).isTrue();
        assertThat(dto.getCountOfRent()).isEqualTo(5L);
        assertThat(dto.getComments()).hasSize(1);
        assertThat(dto.getComments().get(0).getText()).isEqualTo("Great item!");
        assertThat(dto.getComments().get(0).getAuthorName()).isEqualTo("Test Owner");
        assertThat(dto.getLastBooking()).isNotNull();
        assertThat(dto.getNextBooking()).isNotNull();
    }

    @Test
    void shouldMapCreateRequestToEntity() {
        CreateItemRequest request = new CreateItemRequest();
        request.setName("New Item");
        request.setDescription("New Description");
        request.setAvailable(true);
        request.setRequestId(10L);

        Item entity = itemMapper.toEntity(request, owner);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("New Item");
        assertThat(entity.getDescription()).isEqualTo("New Description");
        assertThat(entity.isAvailable()).isTrue();
        assertThat(entity.getOwner()).isEqualTo(owner);
        assertThat(entity.getCountOfRent()).isEqualTo(0L);
        assertThat(entity.getId()).isNull();
        assertThat(entity.getComments()).isEmpty();
    }

    @Test
    void shouldUpdateEntityFromRequest() {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        request.setAvailable(false);

        itemMapper.updateEntity(item, request);

        assertThat(item.getName()).isEqualTo("Updated Name");
        assertThat(item.getDescription()).isEqualTo("Updated Description");
        assertThat(item.isAvailable()).isFalse();
        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getCountOfRent()).isEqualTo(5L);
    }

    @Test
    void shouldUpdateEntityPartially() {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setName("Updated Name");

        itemMapper.updateEntity(item, request);

        assertThat(item.getName()).isEqualTo("Updated Name");
        assertThat(item.getDescription()).isEqualTo("Test Description");
        assertThat(item.isAvailable()).isTrue();
        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getOwner()).isEqualTo(owner);
    }

    @Test
    void shouldHandleNullUpdateRequest() {
        itemMapper.updateEntity(item, null);

        assertThat(item.getName()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("Test Description");
        assertThat(item.isAvailable()).isTrue();
        assertThat(item.getId()).isEqualTo(1L);
    }

    @Test
    void shouldHandleNullItemForDto() {
        ItemDto dto = itemMapper.toDto(null);
        assertThat(dto).isNull();

        OwnerItemDto ownerDto = itemMapper.toDtoOwner(null);
        assertThat(ownerDto).isNull();
    }

    @Test
    void shouldHandleNullOwnerForEntity() {
        CreateItemRequest request = new CreateItemRequest();
        request.setName("New Item");
        request.setDescription("New Description");
        request.setAvailable(true);

        Item entity = itemMapper.toEntity(request, null);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("New Item");
        assertThat(entity.getOwner()).isNull();
        assertThat(entity.getCountOfRent()).isEqualTo(0L);
    }

    @Test
    void shouldHandleNullCreateRequest() {
        Item entity = itemMapper.toEntity(null, owner);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getOwner()).isEqualTo(owner);
        assertThat(entity.getCountOfRent()).isEqualTo(0L);
    }

    @Test
    void shouldHandleNullComments() {
        item.setComments(null);

        ItemDto dto = itemMapper.toDto(item);
        assertThat(dto).isNotNull();
        assertThat(dto.getComments()).isNull();

        OwnerItemDto ownerDto = itemMapper.toDtoOwner(item);
        assertThat(ownerDto).isNotNull();
        assertThat(ownerDto.getComments()).isNull();
    }

    @Test
    void shouldHandleEmptyComments() {
        item.setComments(List.of());

        ItemDto dto = itemMapper.toDto(item);
        assertThat(dto).isNotNull();
        assertThat(dto.getComments()).isEmpty();

        OwnerItemDto ownerDto = itemMapper.toDtoOwner(item);
        assertThat(ownerDto).isNotNull();
        assertThat(ownerDto.getComments()).isEmpty();
    }

    @Test
    void shouldMapItemWithNoBookings() {
        item.setLastBooking(null);
        item.setNextBooking(null);

        OwnerItemDto dto = itemMapper.toDtoOwner(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getLastBooking()).isNull();
        assertThat(dto.getNextBooking()).isNull();
        assertThat(dto.getCountOfRent()).isEqualTo(5L);
    }

    @Test
    void shouldUpdateOnlyAvailableField() {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setAvailable(false);

        itemMapper.updateEntity(item, request);

        assertThat(item.getName()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("Test Description");
        assertThat(item.isAvailable()).isFalse();
        assertThat(item.getId()).isEqualTo(1L);
    }

    @Test
    void shouldCreateEntityWithoutRequestId() {
        CreateItemRequest request = new CreateItemRequest();
        request.setName("New Item");
        request.setDescription("New Description");
        request.setAvailable(true);

        Item entity = itemMapper.toEntity(request, owner);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("New Item");
        assertThat(entity.getDescription()).isEqualTo("New Description");
        assertThat(entity.isAvailable()).isTrue();
        assertThat(entity.getOwner()).isEqualTo(owner);
        assertThat(entity.getCountOfRent()).isEqualTo(0L);
    }
}