package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class ConstantTest {

    @Test
    void shouldReturnPositiveForLaterStartDateAsc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 2, 15, 10)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        assertTrue(Constant.orderByStartDateAsc.compare(booking, booking2) > 0);
    }

    @Test
    void shouldReturnZeroForEqualStartDatesDesc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        assertEquals(0, Constant.orderByStartDateDesc.compare(booking, booking2));
    }

    @Test
    void shouldReturnPositiveForEarlierStartDateDesc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 2, 15, 10)).build();

        assertTrue(Constant.orderByStartDateDesc.compare(booking, booking2) > 0);
    }

    @Test
    void shouldReturnSameResultForEqualBookingsAsc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        int result1 = Constant.orderByStartDateAsc.compare(booking, booking2);
        int result2 = Constant.orderByStartDateAsc.compare(booking2, booking);

        assertEquals(result1, result2);
    }

    @Test
    void shouldReturnSameResultForEqualBookingsDesc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        int result1 = Constant.orderByStartDateDesc.compare(booking, booking2);
        int result2 = Constant.orderByStartDateDesc.compare(booking2, booking);

        assertEquals(result1, result2);
    }

    @Test
    void shouldSortListOfBookingsAsc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 2, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 3, 15, 10)).build();

        BookingItemDto booking3 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 8, 20)).build();


        List<BookingItemDto> bookings = Arrays.asList(booking, booking2, booking3);

        bookings.sort(Constant.orderByStartDateAsc);

        List<BookingItemDto> expectedOrder = List.of(booking3, booking, booking2);
        assertEquals(expectedOrder, bookings);
    }

    @Test
    void shouldSortListOfBookingsDesc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 2, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 3, 15, 10)).build();

        BookingItemDto booking3 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 8, 20)).build();


        List<BookingItemDto> bookings = Arrays.asList(booking, booking2, booking3);

        bookings.sort(Constant.orderByStartDateDesc);

        List<BookingItemDto> expectedOrder = List.of(booking2, booking, booking3);
        assertEquals(expectedOrder, bookings);
    }

    @Test
    void shouldReturnZeroForEqualStartDatesAsc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        assertEquals(0, Constant.orderByStartDateAsc.compare(booking, booking2));
    }

    @Test
    void shouldReturnNegativeForEarlierStartDateAsc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 2, 15, 10)).build();

        assertTrue(Constant.orderByStartDateAsc.compare(booking, booking2) < 0);
    }

    @Test
    void shouldReturnNegativeForLaterStartDateDesc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 2, 15, 10)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        assertTrue(Constant.orderByStartDateDesc.compare(booking, booking2) < 0);
    }

    @Test
    void shouldReturnZeroForEqualStartAndEndDatesAsc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        assertEquals(0, Constant.orderByStartDateAsc.compare(booking, booking2));
    }

    @Test
    void shouldReturnZeroForEqualStartAndEndDatesDesc() {
        BookingItemDto booking = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        BookingItemDto booking2 = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 10, 0)).build();

        assertEquals(0, Constant.orderByStartDateDesc.compare(booking, booking2));
    }
}
