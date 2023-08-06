package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto addItemRequest(Long userId, ItemRequestShortDto shortRequestDto);

    List<ItemRequestDto> findAllOwnItemRequest(Long userId);

    List<ItemRequestDto> findAllItemRequests(Long userId, Integer from, Integer size);

    ItemRequestDto findRequestById(Long userId, Long requestId);

}
