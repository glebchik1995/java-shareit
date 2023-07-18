
package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDto> findAllUsers() {
        log.info("Получен GET-запрос: /users на получение всех пользователей");
        return service.findAllUsers();

    }

    @GetMapping(value = "/{id}")
    public UserDto findUser(@PathVariable Long id) {
        log.info("Получен GET-запрос: /users/{id} на получение пользователя с ID = {}", id);
        return service.findUserById(id);
    }

    @PostMapping
    public UserDto addUser(@Validated(CreateObject.class)
                           @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос: /users на создание пользователя: {}", userDto);
        return service.addUser(userDto);
    }

    @PatchMapping(value = "/{id}")
    public UserDto updateUser(@PathVariable Long id,
                              @Validated(UpdateObject.class)
                              @RequestBody UserDto userDto) {
        log.info("Получен PATCH-запрос: /users/{id} на обновление пользователя с ID = {}", id);
        return service.updateUserById(id, userDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен DELETE-запрос: /users/{id} на удаление пользователя с ID = {}", id);
        service.deleteUserById(id);
    }
}
