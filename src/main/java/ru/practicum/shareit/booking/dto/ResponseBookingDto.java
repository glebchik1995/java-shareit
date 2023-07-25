package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBookingDto {
    private Long id;
    private Long itemId;
    private Long bookerId;
}
