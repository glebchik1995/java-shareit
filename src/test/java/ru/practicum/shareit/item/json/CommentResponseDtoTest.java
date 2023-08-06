package ru.practicum.shareit.item.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentResponseDtoTest {
    @Autowired
    private JacksonTester<CommentResponseDto> tester;

    @Test
    void commentResponseDtoTest() throws IOException {
        CommentResponseDto commentRequestDto = CommentResponseDto.builder()
                .id(1L)
                .text("text")
                .authorName("authorName")
                .created(LocalDateTime.of(2000, 1, 1, 12, 0))
                .build();

        JsonContent<CommentResponseDto> jsonContent = tester.write(commentRequestDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(jsonContent).extractingJsonPathStringValue("$.text")
                .isEqualTo(commentRequestDto.getText());

        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName")
                .isEqualTo(commentRequestDto.getAuthorName());

        assertThat(jsonContent).extractingJsonPathStringValue("$.created")
                .isEqualTo("2000-01-01T12:00:00");

    }
}