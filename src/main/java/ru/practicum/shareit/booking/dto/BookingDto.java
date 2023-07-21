package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@NoArgsConstructor
public class BookingDto {

    private Long id;

    @NotNull(groups = {CreateObject.class})
    private Long itemId;

    private User booker;

    private Item item;

    @Future(message = "Время начала не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    @NotNull
    private LocalDateTime start;

    @Future(message = "Время окончания не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    @NotNull
    private LocalDateTime end;

    private Status status;
}
