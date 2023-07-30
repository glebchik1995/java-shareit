
package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ItemDto {
    @PositiveOrZero
    Long id;

    @NotBlank(message = "name should not be empty", groups = {CreateObject.class})
    @Size(max = 50, message = "max size = 50", groups = {CreateObject.class})
    String name;

    @NotBlank(message = "description should not be empty", groups = {CreateObject.class})
    @Size(min = 1, max = 250, message = "min size = 1; max size = 250", groups = {CreateObject.class})
    String description;

    @NotNull(groups = {CreateObject.class})
    Boolean available;

    Long requestId;

}
