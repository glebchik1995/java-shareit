package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "name should not be empty", groups = {Marker.OnCreate.class})
    private String name;

    @NotEmpty(message = "email should not be empty and contain space", groups = {Marker.OnCreate.class})
    @Email(message = "incorrect email", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String email;
}
