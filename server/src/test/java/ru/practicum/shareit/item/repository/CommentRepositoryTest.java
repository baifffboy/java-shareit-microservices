package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveComment() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(savedUser);
        item.setCountOfRent(0L);
        Item savedItem = itemRepository.save(item);

        Comment comment = new Comment();
        comment.setText("Great item!");
        comment.setItem(savedItem);
        comment.setAuthor(savedUser);
        comment.setCreated(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getText()).isEqualTo("Great item!");
        assertThat(saved.getItem()).isEqualTo(savedItem);
        assertThat(saved.getAuthor()).isEqualTo(savedUser);
    }

    @Test
    void shouldFindCommentById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(savedUser);
        item.setCountOfRent(0L);
        Item savedItem = itemRepository.save(item);

        Comment comment = new Comment();
        comment.setText("Great item!");
        comment.setItem(savedItem);
        comment.setAuthor(savedUser);
        comment.setCreated(LocalDateTime.now());
        Comment saved = commentRepository.save(comment);

        Comment found = commentRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getText()).isEqualTo("Great item!");
    }
}
