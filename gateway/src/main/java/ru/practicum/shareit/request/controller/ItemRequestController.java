package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.util.Constant.*;

@Validated
@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @Validated({CreateObject.class}) @RequestBody ItemRequestShortDto shortRequestDto) {
        log.info("Добавить запрос на вещь");
        return itemRequestClient.addItemRequest(userId, shortRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllOwnItemRequest(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Получить список всех своих запросов на вещь.");
        return itemRequestClient.findAllOwnItemRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                      @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) Integer from,
                                                      @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получить список всех запросов на вещь.");
        return itemRequestClient.findAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@RequestHeader(USER_ID_HEADER) Long userId,
                                                      @PathVariable Long requestId) {
        log.info("Получить запрос по ID = {}", requestId);
        return itemRequestClient.findItemRequestById(userId, requestId);
    }
}
