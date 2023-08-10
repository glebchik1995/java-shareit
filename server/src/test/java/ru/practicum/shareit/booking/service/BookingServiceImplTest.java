package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.BookingState.*;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.util.Constant.SORT_BY_START_DATE_DESC;

class BookingServiceImplTest {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final BookingService bookingService = new BookingServiceImpl(bookingRepository, itemRepository,
            userRepository);
    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {

        booker = User.builder()
                .id(2L)
                .name("name")
                .email("user1@mail.com")
                .build();

        owner = User.builder()
                .id(1L)
                .name("owner")
                .email("user2@mail.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .itemRequest(null)
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        bookingDto = BookingMapper.toBookingDto(booking);

    }

    @Test
    void createBooking() {
        BookingDtoShort bookingDtoS = new BookingDtoShort(bookingDto.getStart(), bookingDto.getEnd(), item.getId());
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        BookingDto result = bookingService.addBooking(bookingDtoS, booker.getId());
        assertNotNull(result);
        assertEquals(bookingDto, result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void requestStatusDecision() {
        when(userRepository.findById(owner.getId()))
                .thenReturn(Optional.of(owner));
        Booking bookingApr = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, booker,
                BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookingApr));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(bookingApr);
        BookingDto result = bookingService.approveBooking(1L, 1L, true);
        assertNotNull(result);
        assertEquals(APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBookingByUser() {
        when(userRepository.findById(owner.getId()))
                .thenReturn(Optional.of(owner));
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        BookingDto result = bookingService.findBookingByUser(1L, 1L);
        assertNotNull(result);
        assertEquals(bookingDto, result);
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllBookingsByUser() {
        when(userRepository.findById(owner.getId()))
                .thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Collection<BookingDto> result = bookingService.findAllBookingsCustomer(1L, ALL, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsCustomer(1L, CURRENT, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsCustomer(1L, PAST, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsCustomer(1L, FUTURE, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any(BookingStatus.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsCustomer(1L, WAITING, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
        result = bookingService.findAllBookingsCustomer(1L, REJECTED, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
        verify(bookingRepository, times(1)).findAllByBookerId(anyLong(), any(Pageable.class));
        verify(bookingRepository, times(1)).findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, times(1)).findAllByBookerIdAndEndBefore(anyLong(),
                any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, times(1)).findAllByBookerIdAndStartAfter(anyLong(),
                any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, times(2)).findAllByBookerIdAndStatusEquals(anyLong(),
                any(BookingStatus.class), any(Pageable.class));
    }

    @Test
    void getAllBookingsByOwner() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwner(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Collection<BookingDto> result = bookingService.findAllBookingsOwner(1L, ALL, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(any(User.class), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsOwner(1L, CURRENT, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByItemOwnerAndEndBefore(any(User.class), any(LocalDateTime.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsOwner(1L, PAST, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByItemOwnerAndStartAfter(any(User.class), any(LocalDateTime.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsOwner(1L, FUTURE, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findAllByItemOwnerAndStatusEquals(any(User.class), any(BookingStatus.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));
        result = bookingService.findAllBookingsOwner(1L, WAITING, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
        result = bookingService.findAllBookingsOwner(1L, REJECTED, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
        verify(userRepository, times(6)).findById(anyLong());
        verify(bookingRepository, times(1)).findAllByItemOwner(any(User.class),
                any(Pageable.class));
        verify(bookingRepository, times(1))
                .findAllByItemOwnerAndStartBeforeAndEndAfter(any(User.class), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, times(1)).findAllByItemOwnerAndEndBefore(any(User.class),
                any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, times(1)).findAllByItemOwnerAndStartAfter(any(User.class),
                any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, times(2)).findAllByItemOwnerAndStatusEquals(any(User.class),
                any(BookingStatus.class), any(Pageable.class));
    }

    @Test
    void getBookingDtoItemTest() {
        when(bookingRepository.findAllByItem(any(Item.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        Collection<BookingItemDto> result = bookingRepository.findAllByItem(item, SORT_BY_START_DATE_DESC).stream()
                .map(BookingMapper::toBookingDtoItem)
                .collect(Collectors.toList());
        assertNotNull(result);
        assertEquals(List.of(BookingMapper.toBookingDtoItem(booking)), result);
        verify(bookingRepository, times(1)).findAllByItem(any(Item.class), any(Sort.class));
    }
}
