package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserMapperTest {
    @Test
    void mapFromUserToUserDtoTest() {

        User user = User.builder()
                .id(1L)
                .build();

        UserDto userDto = UserMapper.toUserDto(user);

        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getId(), equalTo(user.getId()));
    }
}

