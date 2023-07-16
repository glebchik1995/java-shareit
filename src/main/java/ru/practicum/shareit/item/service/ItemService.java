package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long id);

    ItemDto updateItemById(ItemDto itemDto, Long userId, Long itemId);

    ItemWithBookingsDto getItemById(Long userId, Long itemId);

    List<ItemWithBookingsDto> getAllItems(Long userId);

    List<ItemDto> searchItemBySubstring(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto responseCommentDto);
}
