package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @Validated({CreateObject.class}) @RequestBody ItemRequestShortDto shortRequestDto) {
        log.info("Добавить запрос на вещь");
        return requestService.addItemRequest(userId, shortRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllOwnItemRequest(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Получить список всех своих запросов на вещь.");
        return requestService.findAllOwnItemRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllItemRequests(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                                    @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получить список всех запросов на вещь.");
        return requestService.findAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findRequestById(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                          @PathVariable Long requestId) {
        log.info("Получить запрос по ID = {}", requestId);
        return requestService.findRequestById(userId, requestId);
    }
}
