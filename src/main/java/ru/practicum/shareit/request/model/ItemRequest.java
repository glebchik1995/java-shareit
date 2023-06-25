package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ItemRequest {

    private Long id;

    private String description;

    private User requestor;

    private LocalDateTime created;
}
