package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constant.USER_ID_HEADER;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private MockMvc mvc;

    private ObjectMapper mapper;

    private ItemRequest itemRequest;

    private ItemRequestDto itemRequestDto;

    private ItemRequestShortDto itemRequestShortDto;

    private final LocalDateTime created = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();

        mapper = new ObjectMapper();

        itemRequest = ItemRequest.builder()
                .description("description")
                .created(created)
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .description("description")
                .created(created)
                .build();

        itemRequestShortDto = new ItemRequestShortDto("description");
    }

    @Test
    void shouldAddItemRequest() throws Exception {
        when(requestService.addItemRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestShortDto))
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription()), String.class));
    }

    @Test
    void shouldFindAllOwnItemRequest() throws Exception {
        when(requestService.findAllItemRequests(any(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription()), String.class));
    }

    @Test
    void shouldFindAllItemRequests() throws Exception {
        when(requestService.findAllOwnItemRequest(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription()), String.class));
    }

    @Test
    void shouldFindRequestById() throws Exception {
        when(requestService.findRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header(USER_ID_HEADER, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription()), String.class));
    }
}
