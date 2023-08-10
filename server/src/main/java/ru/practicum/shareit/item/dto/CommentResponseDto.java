package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponseDto {
    Long id;
    @NotBlank
    String text;
    String authorName;
    @DateTimeFormat(pattern = TIME_PATTERN)
    LocalDateTime created;
}
