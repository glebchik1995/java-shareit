
package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
public class UserDto {

    private Long id;

    @NotBlank(groups = {CreateObject.class}, message = "name should not be empty")
    private String name;

    @NotBlank(groups = {CreateObject.class}, message = "email should not be empty and contain space")
    @Email(groups = {CreateObject.class, UpdateObject.class}, message = "incorrect email")
    private String email;
}
