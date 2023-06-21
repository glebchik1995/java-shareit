package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {

    @NonNull
    private Long id;

    @Size(max = 50)
    private String name;

    @Size(max = 200)
    private String description;

    private Boolean available;

    @NonNull
    private User owner;

    private ItemRequest itemRequest;
}
