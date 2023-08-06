package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingMapperTest {

    @Test
    void mapFromBookingToBookingDtoTest() {
        User user = User.builder()
                .id(1L)
                .build();

        Item item = Item.builder()
                .id(1L)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .build();

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        assertThat(bookingDto.getId(), equalTo(booking.getId()));
        assertThat(bookingDto.getBooker().getId(), equalTo(booking.getBooker().getId()));
    }
}
