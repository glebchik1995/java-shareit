package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto findUserById(Long id);

    List<UserDto> findAllUsers();

    UserDto updateUserById(Long id, UserDto userDto);

    void deleteUserById(Long id);

}
