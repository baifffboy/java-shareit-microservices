package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;
    private Booking bookingUpdated;
    private BookingDto bookingDto;
    private CreateBookingRequest createRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Booker");

        owner = new User();
        owner.setId(2L);
        owner.setName("Owner");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setAvailable(true);
        item.setOwner(owner);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);

        bookingUpdated = new Booking();
        bookingUpdated.setId(1L);
        bookingUpdated.setStart(booking.getStart());
        bookingUpdated.setEnd(booking.getEnd());
        bookingUpdated.setStatus(Status.APPROVED);
        bookingUpdated.setItem(item);
        bookingUpdated.setBooker(user);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus(Status.APPROVED);

        createRequest = new CreateBookingRequest();
        createRequest.setItemId(1L);
        createRequest.setStart(LocalDateTime.now().plusDays(1));
        createRequest.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingMapper.toBookingCreate(any(CreateBookingRequest.class), any(Item.class), any(User.class)))
                .thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.create(createRequest, 1L);

        assertThat(result).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenItemNotAvailable() {
        item.setAvailable(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.create(createRequest, 1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("забронирована");
    }

    @Test
    void shouldThrowExceptionWhenStartAfterEnd() {
        createRequest.setStart(LocalDateTime.now().plusDays(2));
        createRequest.setEnd(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.create(createRequest, 1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("раньше");
    }

    @Test
    void shouldThrowExceptionWhenStartEqualsEnd() {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        createRequest.setStart(now);
        createRequest.setEnd(now);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.create(createRequest, 1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не могут совпадать");
    }

    @Test
    void shouldPatchBooking() throws Exception {
        // Используем doReturn() для всех моков, чтобы избежать PotentialStubbingProblem
        doReturn(Optional.of(booking)).when(bookingRepository).findById(1L);

        // Мокаем маппер - он должен вернуть обновленное бронирование
        doReturn(bookingUpdated).when(bookingMapper).toBookingUpdate(any(UpdateBookingRequest.class), any(Booking.class));

        // Мокаем сохранение
        doReturn(bookingUpdated).when(bookingRepository).save(any(Booking.class));

        // Мокаем маппер для DTO
        doReturn(bookingDto).when(bookingMapper).toDto(any(Booking.class));

        BookingDto result = bookingService.patch(1L, true, 2L);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.patch(1L, true, 1L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("не является владельцем");
    }

    @Test
    void shouldGetBookingById() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFound() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найдено");
    }

    @Test
    void shouldGetBookingsByLeaseholder() {
        when(bookingRepository.findByBooker_IdOrderByStartDesc(1L))
                .thenReturn(List.of(booking));
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        List<BookingDto> results = bookingService.getBookingByLeaseholder(1L);

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldGetBookingsByOwner() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findByItem_Owner_IdOrderByStartDesc(2L))
                .thenReturn(List.of(booking));
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        List<BookingDto> results = bookingService.getBookingByOwner(2L);

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.getBookingByOwner(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не существует");
    }
}
