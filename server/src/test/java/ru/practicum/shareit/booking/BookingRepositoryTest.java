package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldSaveBooking() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        User savedOwner = userRepository.save(owner);

        User booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        User savedBooker = userRepository.save(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(savedOwner);
        item.setCountOfRent(0L);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        booking.setItem(savedItem);
        booking.setBooker(savedBooker);

        Booking saved = bookingRepository.save(booking);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void shouldFindBookingsByBooker() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        User savedOwner = userRepository.save(owner);

        User booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        User savedBooker = userRepository.save(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(savedOwner);
        item.setCountOfRent(0L);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        booking.setItem(savedItem);
        booking.setBooker(savedBooker);
        bookingRepository.save(booking);

        var bookings = bookingRepository.findByBooker_IdOrderByStartDesc(savedBooker.getId());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getBooker().getId()).isEqualTo(savedBooker.getId());
    }

    @Test
    void shouldFindBookingsByItemOwner() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        User savedOwner = userRepository.save(owner);

        User booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        User savedBooker = userRepository.save(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(savedOwner);
        item.setCountOfRent(0L);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        booking.setItem(savedItem);
        booking.setBooker(savedBooker);
        bookingRepository.save(booking);

        var bookings = bookingRepository.findByItem_Owner_IdOrderByStartDesc(savedOwner.getId());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getItem().getOwner().getId()).isEqualTo(savedOwner.getId());
    }
}
