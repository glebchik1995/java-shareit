package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestServiceImpl requestService;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .requester(user)
                .build();

        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        itemRequestDto.setItems(Collections.emptyList());
    }

    @Test
    void shouldAddItemRequest() {
        ItemRequestShortDto shortRequestDto = new ItemRequestShortDto("description");
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestDto result = requestService.addItemRequest(1L, shortRequestDto);
        assertNotNull(result);
        result.setItems(Collections.emptyList());
        assertEquals(itemRequestDto, result);
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldFindAllOwnItemRequest() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(itemRequest));
        List<ItemRequestDto> result = requestService.findAllOwnItemRequest(1L);
        assertNotNull(result);
        assertEquals(List.of(itemRequestDto), result);
        verify(requestRepository, times(1)).findAllByRequesterId(anyLong(), any(Sort.class));
        verify(itemRepository, times(1)).findAllByItemRequestIn(anyList());
    }

    @Test
    void shouldFindAllItemRequests() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        List<ItemRequestDto> result = requestService.findAllItemRequests(1L, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(itemRequestDto), result);
        verify(requestRepository, times(1)).findAllByRequesterIdNot(anyLong(),
                any(Pageable.class));
        verify(itemRepository, times(1)).findAllByItemRequestIn(anyList());
    }

    @Test
    void shouldFindRequestById() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        ItemRequestDto result = requestService.findRequestById(1L, 1L);
        assertNotNull(result);
        assertEquals(itemRequestDto, result);
        verify(requestRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findAllByItemRequest(any(ItemRequest.class));
    }

}

