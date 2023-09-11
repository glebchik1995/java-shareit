package ru.practicum.shareit.user.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class UserDtoJsonTests {

    @Autowired
    private JacksonTester<UserDto> tester;

    @Test
    void userDtoTest() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(null)
                .name("name")
                .email("practikum@email.ru")
                .build();

        JsonContent<UserDto> jsonContent = tester.write(userDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.id")
                .isEqualTo(userDto.getId());

        assertThat(jsonContent).extractingJsonPathStringValue("$.name")
                .isEqualTo(userDto.getName());

        assertThat(jsonContent).extractingJsonPathStringValue("$.email")
                .isEqualTo(userDto.getEmail());
    }
}
