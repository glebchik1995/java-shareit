package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingItemDto {

    private Long id;

    @NotNull
    @Future(message = "Время начала не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime start;

    @NotNull
    @Future(message = "Время окончания не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime end;

    private ItemDto item;

    private Long bookerId;

    private BookingStatus status;
}
