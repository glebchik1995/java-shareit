package ru.practicum.shareit.request.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> tester;

    @Test
    void itemRequestResponseDtoTest() throws IOException {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(null)
                .items(null)
                .build();

        JsonContent<ItemRequestDto> jsonContent =
                tester.write(itemRequestDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());

        assertThat(jsonContent).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestDto.getCreated());

        assertThat(jsonContent).extractingJsonPathNumberValue("$.items")
                .isEqualTo(itemRequestDto.getItems());
    }
}
