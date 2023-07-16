package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapperUtil mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            User user = mapper.map(userDto, User.class);
            return mapper.map(userRepository.save(user), UserDto.class);
        } catch (ConstraintViolationException exception) {
            throw new DataAlreadyExistException((String.format("Пользователь с ID= %s уже зарегистрирован!",
                    userDto.getEmail())));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserById(Long id) {
        return mapper.map(userRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                String.format("Пользователь с ID = %d не найден", id))), UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        if (userDto.getId() == null) {
            userDto.setId(id);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID= %d не найден!", id)));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (!userDto.getEmail().equals(user.getEmail()))) {
            if (userRepository.findByEmail(userDto.getEmail())
                    .stream()
                    .filter(u -> u.getEmail().equals(userDto.getEmail()))
                    .allMatch(u -> u.getId().equals(userDto.getId()))) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new DataAlreadyExistException((String.format("Пользователь с ID= %s уже зарегистрирован!", user.getEmail())));
            }

        }
        return mapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public void deleteUserById(Long id) {
        if (findUserById(id) == null) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %s не найден", id));
        }
        userRepository.deleteById(id);
    }
}
