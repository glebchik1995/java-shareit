package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
@Data
@AllArgsConstructor
public class UserDto {

    @NonNull
    private Long id;

    @NotBlank(message = "name should not be empty")
    private String name;

    @NotEmpty(message = "email should not be empty")
    @Email(message = "incorrect email")
    private String email;
}
