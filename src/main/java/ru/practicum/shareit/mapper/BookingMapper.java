package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
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
