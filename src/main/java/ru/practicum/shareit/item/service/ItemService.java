package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long id);

    List<ItemDto> findAll(Long userId);

    ItemDto findById(Long id);

    ItemDto update(ItemDto itemDto, Long userId, Long itemId);

    List<ItemDto> search(String text);
}
