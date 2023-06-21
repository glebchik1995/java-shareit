package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserStorageImpl storage;

    public UserServiceImpl(UserStorageImpl storage) {
        this.storage = storage;
    }

    @Override
    public List<UserDto> findAll() {
        return storage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
