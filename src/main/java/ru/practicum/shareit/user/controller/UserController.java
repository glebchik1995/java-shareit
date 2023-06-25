package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
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
        return service.findAll();

    }

    @GetMapping(value = "/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        log.info("Получен GET-запрос: /users/{id} на получение пользователя с ID = {}", id);
        return service.findById(id);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto){
        log.info("Получен POST-запрос: /users на создание пользователя: {}", userDto);
        return service.create(userDto);
    }

    @PatchMapping(value = "/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        log.info("Получен PATCH-запрос: /users/{id} на обновление пользователя с ID = {}", id);
        return service.update(id, userDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserById(@PathVariable Long id){
        log.info("Получен DELETE-запрос: /users/{id} на удаление пользователя с ID = {}", id);
        service.delete(id);
    }
}
