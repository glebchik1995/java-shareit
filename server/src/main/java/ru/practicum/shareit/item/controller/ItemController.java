
package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADER) Long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Получен POST-запрос: /items на добавления item:{}", itemDto.getName());
        return itemService.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody CommentDto commentDto) {
        log.info("Получен POST-запрос: /items/itemId/comment на написание пользователем {} комментария:{}",
                userId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto findItemById(@RequestHeader(USER_ID_HEADER) Long userId,
                                        @PathVariable Long itemId) {
        log.info("Получен GET-запрос:/items/{itemId} на получение вещи по id = {}", itemId);
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemResponseDto> findAllItemsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                    @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получен GET-запрос:/items на получение списка всех вещей");
        return itemService.findAllItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam("text") String text,
                                      @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero Integer from,
                                      @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получен GET-запрос:/items/search на товар в названии или описании которого есть: {}", text);
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemService.searchByText(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен PATCH-запрос:/items/itemId на обновление вещи по id = {}", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

}
