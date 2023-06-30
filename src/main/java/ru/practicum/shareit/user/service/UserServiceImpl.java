package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorageImpl userStorage;

    private final ModelMapperUtil mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        return mapper.map(userStorage.create(user), UserDto.class);
    }

    @Override
    public UserDto findById(Long id) {
        return mapper.map(userStorage.findById(id), UserDto.class);
    }

    @Override
    public List<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        user.setId(id);
        return mapper.map(userStorage.update(user), UserDto.class);
    }

    @Override
    public void delete(Long id) {
        if (findById(id) == null) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %s не найден", id));
        }
        userStorage.delete(id);
    }
}
