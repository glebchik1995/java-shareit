package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponseDto {

    Long id;

    String name;

    String description;

    Boolean available;

    BookingItemDto lastBooking;

    BookingItemDto nextBooking;

    List<CommentResponseDto> comments;

    Long requestId;
}
