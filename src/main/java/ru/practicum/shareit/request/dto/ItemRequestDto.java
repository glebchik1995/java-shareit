package ru.practicum.shareit.request.dto;

import lombok.Data;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ItemRequestDto {

    @NonNull
    private Long id;
    @Size(min = 1, max = 200)
    private String description;

    @NonNull
    private User requestor;

    @NonNull
    private LocalDateTime created;
}
