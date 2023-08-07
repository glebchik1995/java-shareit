package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ItemRequestDto {

    Long id;

    @NotBlank(groups = {CreateObject.class})
    String description;

    @Future(message = "Время окончания не может быть в прошлом")
    @DateTimeFormat(pattern = TIME_PATTERN)
    @NotNull
    LocalDateTime created;

    List<ItemDto> items = new ArrayList<>();
}
