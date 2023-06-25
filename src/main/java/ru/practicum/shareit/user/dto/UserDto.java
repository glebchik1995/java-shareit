package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto {

    private Long id;

    @NotBlank(message = "name should not be empty")
    private String name;

    @NotBlank(message = "email should not be empty and contain space")
    @Email(message = "incorrect email")
    private String email;
}
