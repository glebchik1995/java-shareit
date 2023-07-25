
package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUserById(Long id, UserDto body);

    UserDto findUserById(Long id);

    List<UserDto> findAllUsers();

    void deleteUserById(Long id);
}
