
package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class ItemDto {

    private Long id;

    @NotBlank(message = "name should not be empty", groups = {CreateObject.class})
    @Size(max = 50, message = "max size = 50", groups = {CreateObject.class})
    private String name;

    @NotBlank(message = "description should not be empty", groups = {CreateObject.class})
    @Size(min = 1, max = 250, message = "min size = 1; max size = 250", groups = {CreateObject.class})
    private String description;
    @NotNull(groups = {CreateObject.class})
    private Boolean available;

}
