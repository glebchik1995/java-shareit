package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ItemResponseDto {

    Long id;

    @NotBlank(groups = {CreateObject.class})
    String name;

    @NotBlank(groups = {CreateObject.class})
    String description;

    @NotNull(groups = {CreateObject.class})
    Boolean available;

    BookingItemDto lastBooking;

    BookingItemDto nextBooking;

    List<CommentResponseDto> comments;

    Long requestId;
}
