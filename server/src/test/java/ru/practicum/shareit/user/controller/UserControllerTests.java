package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constant.USER_ID_HEADER;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private UserDto thirdUserDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        firstUserDto = UserDto.builder()
                .name("name")
                .email("practikum@mail.com")
                .build();
        secondUserDto = UserDto.builder()
                .name("")
                .email("mail")
                .build();
        thirdUserDto = UserDto.builder()
                .name("owner")
                .email("owner@yandex.ru")
                .build();
    }

    @Test
    void shouldAddUserTest() throws Exception {
        when(userService.addUser(any()))
                .thenReturn(firstUserDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName())))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail())));
    }

    @Test
    void shouldFindByIdTest() throws Exception {
        when(userService.findUserById(anyLong()))
                .thenReturn(thirdUserDto);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(thirdUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(thirdUserDto.getName())))
                .andExpect(jsonPath("$.email", is(thirdUserDto.getEmail())));

        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    void shouldFindUsersTest() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(List.of(firstUserDto, thirdUserDto));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(firstUserDto.getName())))
                .andExpect(jsonPath("$[0].email", is(firstUserDto.getEmail())))
                .andExpect(jsonPath("$[1].name", is(thirdUserDto.getName())))
                .andExpect(jsonPath("$[1].email", is(thirdUserDto.getEmail())));
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void shouldFindUsersWhenEmptyTest() throws Exception {

        when(userService.findAllUsers())
                .thenReturn(List.of());

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void shouldDeleteTest() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    void shouldUpdateUserTest() throws Exception {
        when(userService.updateUserById(1L, firstUserDto))
                .thenReturn(firstUserDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName())))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail())));
    }
}
