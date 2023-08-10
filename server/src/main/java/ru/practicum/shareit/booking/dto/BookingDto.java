package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    Long id;

    ItemDto item;

    UserDto booker;

    @FutureOrPresent(message = "Время начала не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    @NotNull
    LocalDateTime start;

    @Future(message = "Время окончания не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    @NotNull
    LocalDateTime end;

    BookingStatus status;
}
