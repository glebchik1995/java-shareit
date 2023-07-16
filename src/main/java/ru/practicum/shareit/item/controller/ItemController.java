package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.marker.Marker;
import ru.practicum.shareit.annotation.ToLog;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@ToLog
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Validated(Marker.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Получен POST-запрос: /items на добавления item:{}", itemDto.getName());
        return itemService.createItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен POST-запрос: /items/{itemId}/comment на написание комментария:{} от пользователя {} ",
                commentDto, userId);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @GetMapping(value = "/{itemId}")
    public ItemWithBookingsDto getItemById(Long userId, @PathVariable Long itemId) {
        log.info("Получен GET-запрос:/items/{itemId} на получение вещи по id = {}", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemWithBookingsDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен GET-запрос:/items на получения списка всех вещей");
        return itemService.getAllItems(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItemBySubstring(@RequestParam String text) {
        log.info("Получен GET-запрос:/items/search на поиск вещи, название или описание которой, содержит слово {}", text);
        return itemService.searchItemBySubstring(text);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                              @Validated(Marker.OnUpdate.class) @RequestBody ItemDto itemDto) {
        log.info("Получен PATCH-запрос:/items/search на обновление вещи по ID = {}", itemId);
        return itemService.updateItemById(itemDto, userId, itemId);
    }

}
