package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDtoShort bookingDtoShort, Long userId);

    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto findBookingByUser(Long userId, Long bookingId);

    List<BookingDto> findAllBookingsCustomer(Long userId, BookingState state, Integer from, Integer size);

    List<BookingDto> findAllBookingsOwner(Long ownerId, BookingState state, Integer from, Integer size);
}
