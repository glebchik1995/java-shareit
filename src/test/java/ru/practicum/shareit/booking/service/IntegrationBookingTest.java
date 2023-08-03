package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IntegrationBookingTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final EntityManager entityManager;

    private Item item;
    private UserDto userDto;
    private ItemDto itemDto;
    private User user;
    private BookingDtoShort bookingDto;
    private static LocalDateTime start;
    private static LocalDateTime end;

    @BeforeAll
    static void beforeAll() {
        start = LocalDateTime.MIN;
        end = LocalDateTime.MAX;
    }

    @BeforeEach
    void setUp() {

        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        user = User.builder()
                .name("name")
                .email("email@email.ru")
                .build();

        userDto = UserDto.builder()
                .name("name")
                .email("email@email.ru")
                .build();

        bookingDto = BookingDtoShort.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();

        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();
    }

    @Test
    @DirtiesContext
    void shouldAddBooking() {
        userService.addUser(userDto);

        UserDto secondUser = UserDto.builder()
                .email("second@email.ru")
                .name("name")
                .build();


        userService.addUser(secondUser);

        itemService.addItem(1L, itemDto);

        bookingService.addBooking(bookingDto, 2L);

        TypedQuery<Booking> query = entityManager
                .createQuery("select b from Booking b where b.id = :id", Booking.class);

        user.setId(1L);
        item.setId(1L);
        Booking bookingFromDb = query.setParameter("id", 1L).getSingleResult();
        assertThat(bookingFromDb.getStart(), equalTo(start));
        assertThat(bookingFromDb.getEnd(), equalTo(end));
        assertThat(bookingFromDb.getStatus(), equalTo(WAITING));
    }

    @Test
    @DirtiesContext
    void shouldFindBookingsByUserId() {
        userService.addUser(userDto);

        UserDto secondUser = UserDto.builder()
                .email("second@email.ru")
                .name("name")
                .build();

        userService.addUser(secondUser);
        itemService.addItem(1L, itemDto);
        bookingService.addBooking(bookingDto, 2L);

        Collection<BookingDto> bookings = bookingService.findAllBookingsCustomer(2L, BookingState.ALL, 0, 20);

        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @DirtiesContext
    void shouldUpdateAvailableStatus() {
        userService.addUser(userDto);

        UserDto secondUser = UserDto.builder()
                .email("second@email.ru")
                .name("name")
                .build();

        userService.addUser(secondUser);
        itemService.addItem(1L, itemDto);
        bookingService.addBooking(bookingDto, 2L);

        bookingService.approveBooking(1L, 1L, true);

        TypedQuery<Booking> query = entityManager
                .createQuery("select b from Booking b where b.id = :id", Booking.class);

        Booking bookingFromDb = query.setParameter("id", 1L).getSingleResult();
        assertThat(bookingFromDb.getStatus(), equalTo(APPROVED));
    }

    @Test
    @DirtiesContext
    void shouldFindAllBookingsByUserId() {
        userService.addUser(userDto);

        UserDto secondUser = UserDto.builder()
                .email("second@email.ru")
                .name("name")
                .build();

        userService.addUser(secondUser);
        itemService.addItem(1L, itemDto);
        bookingService.addBooking(bookingDto, 2L);

        BookingDto newBooking = bookingService.findBookingByUser(1L, 1L);

        secondUser.setId(2L);
        assertThat(newBooking.getBooker(), equalTo(secondUser));
    }

    @Test
    @DirtiesContext
    void shouldFindOwnerBookings() {
        userService.addUser(userDto);

        UserDto secondUser = UserDto.builder()
                .email("second@email.ru")
                .name("name")
                .build();

        userService.addUser(secondUser);
        itemService.addItem(1L, itemDto);
        bookingService.addBooking(bookingDto, 2L);

        List<BookingDto> bookings = bookingService.findAllBookingsOwner(1L, BookingState.WAITING, 0, 20);

        assertThat(bookings.size(), equalTo(1));
    }

}


