package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

@Validated
@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(CreateObject.class) @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос: /users на создание пользователя: {}", userDto);
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @Validated(UpdateObject.class) @RequestBody UserDto userDto) {
        log.info("Получен PATCH-запрос: /users/{id} на обновление пользователя с ID = {}", userId);
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Получен GET-запрос: /users на получение всех пользователей");
        return userClient.findAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable Long userId) {
        log.info("Получен GET-запрос: /users/{id} на получение пользователя с ID = {}", userId);
        return userClient.findUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userId) {
        log.info("Получен DELETE-запрос: /users/{id} на удаление пользователя с ID = {}", userId);
        return userClient.deleteUserById(userId);
    }
}
