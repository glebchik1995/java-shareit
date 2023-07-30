
package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemResponseDto findItemById(Long userId, Long itemId);

    List<ItemResponseDto> findAllItemsByUser(Long userId, Integer from, Integer size);

    List<ItemDto> searchByText(String text, Integer from, Integer size);

    CommentResponseDto addComment(Long userId, Long itemId, CommentDto commentDto);
}

