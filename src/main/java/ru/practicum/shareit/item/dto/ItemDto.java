package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ItemDto {

    private Long id;

    @Size(max = 50, message = "max size = 50")
    @NotBlank(message = "name should not be empty")
    private String name;

    @Size(max = 200, message = "max size = 200")
    @NotBlank(message = "description should not be empty")
    private String description;

    private Boolean available;

}
