package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemRequestMapperTest {
    @Test
    void mapFromItemRequestToItemRequestDtoTest() {

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .build();

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertThat(itemRequestDto.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestDto.getId(), equalTo(itemRequest.getId()));
    }
}
