package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveRequest() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        ItemRequest request = new ItemRequest();
        request.setDescription("Need a drill");
        request.setCreated(LocalDateTime.now());
        request.setRequestUser(savedUser);

        ItemRequest saved = requestRepository.save(request);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescription()).isEqualTo("Need a drill");
    }

    @Test
    void shouldFindRequestsByUserId() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        ItemRequest request = new ItemRequest();
        request.setDescription("Need a drill");
        request.setCreated(LocalDateTime.now());
        request.setRequestUser(savedUser);
        requestRepository.save(request);

        var requests = requestRepository.findByRequestUserIdOrderByCreatedDesc(savedUser.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getRequestUser().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void shouldFindRequestsByOtherUsers() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        User savedUser2 = userRepository.save(user2);

        ItemRequest request = new ItemRequest();
        request.setDescription("Need a drill");
        request.setCreated(LocalDateTime.now());
        request.setRequestUser(savedUser1);
        requestRepository.save(request);

        var requests = requestRepository.findRequestsByOtherUsers(savedUser2.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getRequestUser().getId()).isEqualTo(savedUser1.getId());
    }
}
