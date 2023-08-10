package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoShort {

    @NotNull
    @FutureOrPresent(message = "Время начала не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    LocalDateTime start;

    @NotNull
    @Future(message = "Время окончания не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    LocalDateTime end;

    @PositiveOrZero(message = "id вещи не может быть меньше ноля")
    @NotNull(groups = {CreateObject.class})
    private Long itemId;
}

