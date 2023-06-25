package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorageImpl;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorageImpl itemStorage;

    private final UserStorageImpl userStorage;

    private final ModelMapperUtil mapper;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        Item item = mapper.map(itemDto, Item.class);
        User user = userStorage.findById(userId);
        item.setOwner(user);
        return mapper.map(itemStorage.create(item), ItemDto.class);
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        return itemStorage.findAll().stream()
                .filter(item -> item.getOwner() == userStorage.findById(userId))
                .map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long id) {
        return mapper.map(itemStorage.findById(id), ItemDto.class);

    }

    @Override
    public ItemDto update(ItemDto itemDto, Long userId, Long itemId) {
        Item item = itemStorage.findById(itemId);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId));
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return mapper.map(itemStorage.update(item), ItemDto.class);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemStorage.search(text)
                .stream()
                .map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }
}
