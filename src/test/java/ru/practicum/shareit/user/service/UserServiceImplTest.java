package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("practikum@email.ru")
                .build();

        userDto = UserMapper.toUserDto(user);
    }

    @Test
    void shouldCreateUser() {
        when(userRepository.save(any(User.class)))
                .thenReturn(UserMapper.toUserModel(userDto));
        UserDto result = userService.addUser(userDto);
        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldFindUserById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto result = userService.findUserById(1L);
        assertNotNull(result);
        assertEquals(result, UserMapper.toUserDto(user));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldFindAllUsers() {
        User secondUser = new User(2L, "name2", "user2@mail.com");
        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));
        List<User> result = userService.findAllUsers().stream()
                .map(UserMapper::toUserModel)
                .collect(Collectors.toList());
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result, List.of(user, secondUser));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldUpdateUser() {
        User existingUser = new User(1L, "name", "custams@yandex.ru");
        when(userRepository.findById(existingUser.getId()))
                .thenReturn(Optional.of(existingUser));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(existingUser);

        UserDto updatedUserDto = new UserDto(1L, "Даша", "dasha@yandex.ru");
        UserDto result = userService.updateUserById(existingUser.getId(), updatedUserDto);

        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());

        verify(userRepository).findById(existingUser.getId());
        verify(userRepository).save(any(User.class));

        User savedUser = userCaptor.getValue();
        assertEquals(existingUser.getId(), savedUser.getId());
        assertEquals(updatedUserDto.getName(), savedUser.getName());
        assertEquals(updatedUserDto.getEmail(), savedUser.getEmail());
    }

    @Test
    void shouldDeleteUserById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
