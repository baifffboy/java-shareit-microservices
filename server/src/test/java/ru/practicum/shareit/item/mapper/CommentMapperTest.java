package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void shouldMapCommentToDto() {
        User author = new User();
        author.setId(1L);
        author.setName("Test Author");

        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Great item!");
        assertThat(dto.getAuthorName()).isEqualTo("Test Author");
        assertThat(dto.getCreated()).isNotNull();
    }

    @Test
    void shouldHandleNullAuthor() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(null);
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAuthorName()).isNull();
    }

    @Test
    void shouldHandleNullComment() {
        CommentDto dto = commentMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void shouldHandleNullAuthorName() {
        User author = new User();
        author.setId(1L);
        author.setName(null);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getAuthorName()).isNull();
    }
}