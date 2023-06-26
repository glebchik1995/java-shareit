package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ItemDto {

    private Long id;

    @NotBlank(message = "name should not be empty", groups = {Marker.OnCreate.class})
    @Size(max = 50, message = "max size = 50", groups = {Marker.OnCreate.class})
    private String name;

    @NotBlank(message = "description should not be empty", groups = {Marker.OnCreate.class})
    @Size(min = 1, max = 200, message = "min size = 1; max size = 200", groups = {Marker.OnCreate.class})
    private String description;

    private Boolean available;

}
