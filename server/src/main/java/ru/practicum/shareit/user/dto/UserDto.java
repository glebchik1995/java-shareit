
package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    @NotBlank(groups = {CreateObject.class}, message = "name should not be empty")
    String name;

    @NotBlank(groups = {CreateObject.class}, message = "email should not be empty and contain space")
    @Email(groups = {CreateObject.class, UpdateObject.class}, message = "incorrect email")
    String email;
}
