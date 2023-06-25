package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemStorageImpl implements ItemStorage {

    private long id = 0L;

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        if ((item.getName().isEmpty())
                || (item.getDescription().isEmpty())
                || (item.getAvailable() == null)) {
            throw new ValidationException("У вещи есть незаполненные поля");
        }
        item.setId(++id);
        items.put(item.getId(), item);
        log.info("Вещь: {} добавлена", item.getName());
        return item;
    }

    @Override
    public List<Item> findAll() {
        log.info("Текущее количество вещей: {}", items.size());
        return new ArrayList<>(items.values());
    }

    @Override
    public Item findById(Long itemId) {
        log.info("Возвращаем вещь с ID: {}", itemId);
        return items.values()
                .stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с ID = %d не найдена", id)));
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        log.info("Вещь: {} обновлена", item.getName());
        return item;
    }

    @Override
    public List<Item> search(String text) {
        log.info("Возвращаем вещь, название или описание которой, содержит слово: {}", text);
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
