package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.util.Constant.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен POST-запрос: /bookings на бронирование {}", bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingByUserOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получен GET-запрос: /bookings/bookingId на бронирование по ID: {}", bookingId);
        return bookingService.findBookingByUserOwner(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен GET-запрос: /bookings на получение списка всех бронирований пользователя с ID: {}", userId);
        return bookingService.findUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен GET-запрос: /bookings/owner на получение списка бронирований пользователя с ID: {}", userId);
        return bookingService.getByOwner(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        log.info("Получен PATCH-запрос: /bookings/bookingId на подтверждение запроса на бронирование");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

}
