package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        User saved = userRepository.save(user);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldFindUserById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User saved = userRepository.save(user);

        User found = userRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test User");
    }

    @Test
    void shouldDeleteUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User saved = userRepository.save(user);

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}
