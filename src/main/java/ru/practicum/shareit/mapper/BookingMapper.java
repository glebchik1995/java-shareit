package ru.practicum.shareit.mapper;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
@Repository
public class BookingMapper {
    public ResponseBookingDto toResponseBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return ResponseBookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

}
