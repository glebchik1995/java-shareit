package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationBookingTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    private UserDto owner, booker;

    private ItemDto item;

    private BookingDtoShort bookingShort;

    @BeforeEach
    void setUp() {

        LocalDateTime now = LocalDateTime.of(2024, 12, 12, 10, 0, 0);

        owner = UserDto.builder()
                .name("owner")
                .email("owner@mail.com")
                .build();

        booker = UserDto.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();

        item = ItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();

        bookingShort = BookingDtoShort.builder()
                .itemId(1L)
                .start(now)
                .end(now.plusDays(8))
                .build();
    }

    @Test
    public void getItemByIdTest() {
        UserDto savedUser = userService.addUser(owner);
        UserDto savedBooker = userService.addUser(booker);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), item);
        BookingDto bookingDto = bookingService.addBooking(bookingShort, savedBooker.getId());
        BookingDto gotBooking = bookingService.findBookingByUser(savedBooker.getId(), bookingDto.getId());
        assertThat(gotBooking.getId(), notNullValue());
        assertThat(gotBooking.getItem(), equalTo(savedItem));
        assertThat(gotBooking.getBooker(), equalTo(savedBooker));
        assertThat(gotBooking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(gotBooking.getStart(), equalTo(bookingShort.getStart()));
        assertThat(gotBooking.getEnd(), equalTo(bookingShort.getEnd()));
        userService.deleteUserById(savedUser.getId());
        userService.deleteUserById(savedBooker.getId());
    }

    @Test
    public void exceptionTest() {
        UserDto savedUser = userService.addUser(owner);
        UserDto savedBooker = userService.addUser(booker);
        BookingDtoShort bookingDtoShort = bookingShort;
        try {
            bookingService.addBooking(bookingShort, savedBooker.getId());
        } catch (DataNotFoundException e) {
            assertThat(e.getMessage(), equalTo("Вещи с id = 1 не существует"));
        }
        ItemDto savedItem = itemService.addItem(savedUser.getId(), item);
        bookingDtoShort.setItemId(savedItem.getId());
        savedItem.setAvailable(false);
        itemService.updateItem(savedUser.getId(), savedItem.getId(), savedItem);
        try {
            bookingService.addBooking(bookingShort, savedBooker.getId());
        } catch (DataNotFoundException e) {
            assertThat(e.getMessage(), equalTo("В бронировании отказано. Нельзя бронировать свою вещь"));
        }
        userService.deleteUserById(savedUser.getId());
        userService.deleteUserById(savedBooker.getId());
    }
}
