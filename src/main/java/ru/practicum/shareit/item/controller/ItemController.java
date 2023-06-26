package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.marker.Marker;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Validated(Marker.OnCreate.class) ItemDto itemDto) {
        log.info("Получен POST-запрос: /items на добавления item:{}", itemDto.getName());
        return itemService.create(itemDto, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Получен GET-запрос:/items/{itemId} на получение вещи по id = {}", itemId);
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен GET-запрос:/items на получения списка всех вещей");
        return itemService.findAll(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchBySubstring(@RequestParam String text) {
        log.info("Получен GET-запрос:/items/search на поиск вещи, название или описание которой, содержит слово {}", text);
        return itemService.search(text);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                              @RequestBody @Validated(Marker.OnUpdate.class) ItemDto itemDto) {
        log.info("Получен PATCH-запрос:/items/search на обновление вещи по ID = {}", itemId);
        return itemService.update(itemDto, userId, itemId);
    }
}
