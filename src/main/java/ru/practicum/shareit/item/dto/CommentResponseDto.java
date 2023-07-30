package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constant.TIME_PATTERN;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentResponseDto {
    private Long id;
    @NotBlank
    private String text;
    private String authorName;
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalDateTime created;
}
