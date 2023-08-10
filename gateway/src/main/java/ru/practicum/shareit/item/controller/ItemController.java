
package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.util.Constant.*;

@Validated
@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @Validated({CreateObject.class}) @RequestBody ItemDto itemDto) {
        log.info("Получен POST-запрос: /items на добавления item:{}", itemDto.getName());
        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @Validated(UpdateObject.class) @RequestBody ItemDto itemDto) {
        log.info("Получен PATCH-запрос:/items/itemId на обновление вещи по id = {}", itemId);
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                     @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) Integer from,
                                                     @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получен GET-запрос:/items на получение списка всех вещей");
        return itemClient.findAllItemsByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        System.out.println(userId + itemId);
        log.info("Получен GET-запрос:/items/{itemId} на получение вещи по id = {}", itemId);
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam String text,
                                               @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) Integer from,
                                               @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получен GET-запрос:/items/search на товар в названии или описании которого есть: {}", text);
        return itemClient.searchByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @Validated({CreateObject.class}) @RequestBody CommentDto commentDto) {
        log.info("Получен POST-запрос: /items/itemId/comment на написание пользователем {} комментария:{}",
                userId, commentDto);
        return itemClient.addComment(commentDto, userId, itemId);
    }
}
