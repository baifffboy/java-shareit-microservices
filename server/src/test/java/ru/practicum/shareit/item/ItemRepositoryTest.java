package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveItem() {
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

        Item saved = itemRepository.save(item);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Item");
    }

    @Test
    void shouldFindItemsByOwnerId() {
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
        itemRepository.save(item);

        var items = itemRepository.findByOwnerId(savedUser.getId());

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getOwner().getId()).isEqualTo(savedUser.getId());
    }
}
