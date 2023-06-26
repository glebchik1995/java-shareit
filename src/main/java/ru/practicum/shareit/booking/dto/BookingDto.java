package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    @NonNull
    private Long id;

    @NonNull
    private LocalDateTime start;

    @NonNull
    private LocalDateTime end;

    @NonNull
    private BookingStatus status;

    @NonNull
    private Item item;

    @NonNull
    private User booker;
}
