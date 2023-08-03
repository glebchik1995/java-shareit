package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.DataAlreadyExistException;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationUserTest {

    private final UserService userService;

    private UserDto userDto, userDto2;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("name")
                .email("email@email.ru")
                .build();

        userDto2 = UserDto.builder()
                .name("name2")
                .email("email2@email.ru")
                .build();
    }

    @Test
    void shouldAddUserTest() {
        UserDto createdUserDto = userService.addUser(userDto);
        assertThat(createdUserDto.getName(), equalTo(userDto.getName()));
        assertThat(createdUserDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void shouldFindUserByIdTest() {
        UserDto savedUser = userService.addUser(userDto);
        UserDto gottenUser = userService.findUserById(savedUser.getId());
        assertThat(gottenUser.getId(), notNullValue());
        assertThat(gottenUser.getName(), equalTo(userDto.getName()));
        assertThat(gottenUser.getEmail(), equalTo(userDto.getEmail()));
        userService.deleteUserById(savedUser.getId());
    }

    @Test
    void shouldFindAllUsersTest() {
        UserDto createdUserDto = userService.addUser(userDto);
        UserDto createdUserDto2 = userService.addUser(userDto2);

        List<UserDto> newUserDtoList = userService.findAllUsers();

        assertThat(newUserDtoList.size(), equalTo(2));
        assertThat(newUserDtoList.get(0).getId(), equalTo(createdUserDto.getId()));
        assertThat(newUserDtoList.get(1).getId(), equalTo(createdUserDto2.getId()));
    }

    @Test
    void shouldDeleteUserTest() {
        UserDto createdUserDto = userService.addUser(userDto);

        userService.deleteUserById(createdUserDto.getId());

        assertThrows(DataNotFoundException.class,
                () -> userService.findUserById(createdUserDto.getId()));
    }

    @Test
    void shouldUpdateUserTest() {
        UserDto createdUserDto = userService.addUser(userDto);
        userService.addUser(userDto2);

        createdUserDto.setName("Updated");

        userService.updateUserById(createdUserDto.getId(), createdUserDto);

        UserDto updatedUserDto = userService.findUserById(createdUserDto.getId());

        assertThat(updatedUserDto.getName(), equalTo("Updated"));

        assertThrows(DataNotFoundException.class,
                () -> userService.updateUserById(100L, createdUserDto));

        createdUserDto.setEmail("email2@email.ru");
        assertThrows(DataAlreadyExistException.class,
                () -> userService.updateUserById(createdUserDto.getId(), createdUserDto));
    }
}
