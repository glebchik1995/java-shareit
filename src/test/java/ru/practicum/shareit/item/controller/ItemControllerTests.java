package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemControllerTests {

    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    MockMvc mvc;

    ObjectMapper mapper;

    ItemDto itemDto;

    ItemResponseDto itemResponseDto;

    CommentResponseDto commentResponseDto;

    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        mapper = new ObjectMapper();

        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        itemResponseDto = ItemResponseDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        commentResponseDto = CommentResponseDto.builder()
                .text("text")
                .authorName("name")
                .build();

        commentDto = new CommentDto("text");
    }

    @Test
    void shouldAddItem() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldFindItemById() throws Exception {
        when(itemService.findItemById(anyLong(), anyLong()))
                .thenReturn(itemResponseDto);

        mvc.perform(get("/items/{itemId}", 1)
                        .header(USER_ID_HEADER, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldAllItemsByUser() throws Exception {
        when(itemService.findAllItemsByUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemResponseDto));

        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].name", is(itemResponseDto.getName()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemResponseDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldSearchByText() throws Exception {
        when(itemService.searchByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .header(USER_ID_HEADER, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].name", is(itemResponseDto.getName()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemResponseDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldAddComment() throws Exception {

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentResponseDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentDto.getText()));
    }
}
