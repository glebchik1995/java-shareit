package ru.practicum.shareit.item.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> tester;

    @Test
    void commentDtoTest() throws IOException {
        CommentDto commentRequestDto = CommentDto.builder()
                .text("text")
                .build();

        JsonContent<CommentDto> jsonContent = tester.write(commentRequestDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.text")
                .isEqualTo(commentRequestDto.getText());
    }
}
