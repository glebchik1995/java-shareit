package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.DataAlreadyExistException;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        User userFromRepository = userRepository.save(user);
        return UserMapper.toUserDto(userFromRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserById(Long userId) {
        return UserMapper.toUserDto(isExistUser(userId));
    }

    @Override
    public UserDto updateUserById(Long userId, UserDto userDto) {
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        User user = isExistUser(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (!userDto.getEmail().equals(user.getEmail()))) {
            if (userRepository.findUserByEmail(userDto.getEmail())
                    .stream()
                    .filter(u -> u.getEmail().equals(userDto.getEmail()))
                    .allMatch(u -> u.getId().equals(userDto.getId()))) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new DataAlreadyExistException(String.format("Пользователь с email = %s уже существует",
                        userDto.getEmail()));
            }

        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long userId) {
        isExistUser(userId);
        userRepository.deleteById(userId);
    }

    private User isExistUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
    }
}
