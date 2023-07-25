package ru.practicum.shareit.mapper;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
@Builder
public class ItemMapper {

    public static ItemDtoWithBookings toItemDtoWithBookings(Item item, ResponseBookingDto last,
                                                            ResponseBookingDto next, List<CommentDto> comments) {
        return ItemDtoWithBookings.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(last)
                .nextBooking(next)
                .comments(comments)
                .build();
    }
}
