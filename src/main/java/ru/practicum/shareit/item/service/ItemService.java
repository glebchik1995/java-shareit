
package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;

import java.util.List;

public interface ItemService {
    ItemDto addItemById(ItemDto itemDto, Long id);

    ItemDto updateItemById(ItemDto itemDto, Long userId, Long itemId);

    ItemDtoWithBookings findItemById(Long userId, Long itemId);

    List<ItemDtoWithBookings> findAllItems(Long userId);

    List<ItemDto> searchByText(Long userId, String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto responseCommentDto);
}

