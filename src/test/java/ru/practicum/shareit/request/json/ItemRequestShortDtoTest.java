package ru.practicum.shareit.request.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestShortDtoTest {
    @Autowired
    private JacksonTester<ItemRequestShortDto> tester;

    @Test
    void itemRequestRequestDtoTest() throws IOException {
        ItemRequestShortDto itemRequestShortDto = ItemRequestShortDto.builder()
                .description("description")
                .build();

        JsonContent<ItemRequestShortDto> jsonContent =
                tester.write(itemRequestShortDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestShortDto.getDescription());
    }
}
