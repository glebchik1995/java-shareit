package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);
    List<Item> findAll();
    Item findById(Long ItemId);
    Item update(Item item);
    List<Item> search(String text);
}
