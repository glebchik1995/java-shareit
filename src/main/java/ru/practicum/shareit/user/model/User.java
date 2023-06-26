package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class User {

    private Long id;

    private String name;

    private String email;
}
