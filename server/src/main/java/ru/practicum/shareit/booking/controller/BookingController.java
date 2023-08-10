package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.util.Constant.*;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @Valid @Validated(CreateObject.class)
                                 @RequestBody BookingDtoShort bookingDtoShort) {
        log.info("Получен POST-запрос: /bookings на бронирование {}", bookingDtoShort);
        return bookingService.addBooking(bookingDtoShort, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                        @PathVariable Long bookingId) {
        log.info("Получен GET-запрос: /bookings на получение списка всех бронирований пользователя с ID: {}", userId);
        return bookingService.findBookingByUser(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findAllBookingsCustomer(@RequestHeader(USER_ID_HEADER) Long userId,
                                                    @RequestParam(value = "state", defaultValue = "ALL", required = false) BookingState state,
                                                    @RequestParam(value = "from", required = false, defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", required = false, defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        log.info("Получен GET-запрос: /bookings на получение списка всех бронирований пользователя с ID: {}", userId);
        return bookingService.findAllBookingsCustomer(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingsOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam(value = "state", defaultValue = "ALL", required = false) BookingState state,
                                                 @RequestParam(value = "from", required = false, defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero Integer from,
                                                 @RequestParam(value = "size", required = false, defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        return bookingService.findAllBookingsOwner(userId, state, from / size, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        log.info("Получен PATCH-запрос: /bookings/bookingId на подтверждение запроса на бронирование");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

}
