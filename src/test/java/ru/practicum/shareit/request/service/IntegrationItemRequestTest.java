package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemRequestTest {

    private final UserServiceImpl userService;
    private final RequestService requestService;
    private final ItemServiceImpl itemService;

    private final User user = new User(null, "nane", "email@email.ru");
    private final ItemRequestShortDto request = new ItemRequestShortDto("description");


    @Test
    void getWrongUserRequestsTest() {
        assertThrows(DataNotFoundException.class,
                () -> requestService.findAllOwnItemRequest(5L));
    }

    @Test
    void getAllItemWrongUserTest() {
        assertThrows(DataNotFoundException.class,
                () -> requestService.findAllItemRequests(5L, 0, 10));
    }

    @Test
    void getRequestByWrongIdTest() {
        UserDto userDto = userService.addUser(UserMapper.toUserDto(user));
        user.setId(userDto.getId());

        assertThrows(DataNotFoundException.class,
                () -> requestService.findRequestById(user.getId(), 5L));
    }

}
