
package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.util.Constant.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADER) Long userId,
                           @Validated(CreateObject.class)
                           @RequestBody ItemDto itemDto) {
        log.info("Получен POST-запрос: /items на добавления item:{}", itemDto.getName());
        return itemService.addItemById(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен POST-запрос: /items/itemId/comment на написание пользователем {} комментария:{}",
                userId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookings findItemById(@RequestHeader(USER_ID_HEADER) Long userId,
                                            @PathVariable Long itemId) {
        log.info("Получен GET-запрос:/items/{itemId} на получение вещи по id = {}", itemId);
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookings> findAllItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Получен GET-запрос:/items на получение списка всех вещей");
        return itemService.findAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(USER_ID_HEADER) long userId,
                                @RequestParam String text) {
        log.info("Получен GET-запрос:/items/search на товар в названии или описании которого есть: {}", text);
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemService.searchByText(userId, text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                              @PathVariable Long itemId,
                              @Validated(UpdateObject.class)
                              @RequestBody ItemDto itemDto) {
        log.info("Получен PATCH-запрос:/items/itemId на обновление вещи по id = {}", itemId);
        return itemService.updateItemById(itemDto, userId, itemId);
    }

}