package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.marker.Marker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDto> findAllUsers() {
        log.info("Получен GET-запрос: /users на получение всех пользователей");
        return service.findAllUsers();

    }

    @GetMapping(value = "/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        log.info("Получен GET-запрос: /users/{id} на получение пользователя с ID = {}", id);
        return service.findUserById(id);
    }

    @PostMapping
    public UserDto createUser(@Validated(Marker.OnCreate.class) @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос: /users на создание пользователя: {}", userDto);
        return service.createUser(userDto);
    }

    @PatchMapping(value = "/{id}")
    public UserDto updateUserById(@PathVariable Long id, @Validated(Marker.OnUpdate.class) @RequestBody UserDto userDto) {
        log.info("Получен PATCH-запрос: /users/{id} на обновление пользователя с ID = {}", id);
        return service.updateUserById(id, userDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserById(@PathVariable Long id) {
        log.info("Получен DELETE-запрос: /users/{id} на удаление пользователя с ID = {}", id);
        service.deleteUserById(id);
    }
}
