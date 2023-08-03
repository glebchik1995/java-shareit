package ru.practicum.shareit.item.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemResponseDtoTest {
    @Autowired
    private JacksonTester<ItemResponseDto> tester;

    @Test
    void itemResponseDtoTest() throws IOException {
        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .requestId(1L)
                .build();

        JsonContent<ItemResponseDto> jsonContent = tester.write(itemResponseDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(jsonContent).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemResponseDto.getName());

        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemResponseDto.getDescription());

        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemResponseDto.getAvailable());

        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking")
                .isEqualTo(itemResponseDto.getLastBooking());

        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking")
                .isEqualTo(itemResponseDto.getNextBooking());

        assertThat(jsonContent).extractingJsonPathStringValue("$.comments")
                .isEqualTo(itemResponseDto.getComments());

        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(1);
    }
}
