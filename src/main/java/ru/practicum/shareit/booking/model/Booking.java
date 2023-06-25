package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Booking {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;
}
