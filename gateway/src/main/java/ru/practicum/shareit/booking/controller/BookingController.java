package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validation.CreateObject;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.util.Constant.*;

@Validated
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @Valid @Validated(CreateObject.class) @RequestBody BookingDtoShort bookingDtoShort) {
        log.info("Получен POST-запрос: /bookings на бронирование {}", bookingDtoShort);
        return bookingClient.addBooking(bookingDtoShort, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                    @PathVariable Long bookingId) {
        log.info("Получен GET-запрос: /bookings на получение списка всех бронирований пользователя с ID: {}", userId);
        return bookingClient.findBookingByUser(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam Boolean approved) {
        log.info("Получен PATCH-запрос: /bookings/bookingId на подтверждение запроса на бронирование");
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsCustomer(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                          @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) Integer from,
                                                          @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        BookingState state = BookingState.from(stateParam);
        log.info("Получен GET-запрос: /bookings на получение списка всех бронирований пользователя с ID: {}", userId);
        return bookingClient.findAllBookingsCustomer(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                        @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) Integer from,
                                                        @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive Integer size) {
        BookingState state = BookingState.from(stateParam);
        log.info("Get booking with userId={}, state={}, from={}, size={}", ownerId, stateParam, from, size);
        return bookingClient.getBookingsByOwner(ownerId, state, from, size);
    }
}
