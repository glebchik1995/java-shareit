package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.model.State;


import java.util.List;

public interface BookingService {
    BookingDto addBooking(Long userId, BookingDto bookingDto);

    BookingDto findBookingByUserOwner(Long userId, Long bookingId);

    List<BookingDto> getByOwner(Long userId, State state);

    List<BookingDto> findUserBookings(Long userId, State state);

    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);
}
