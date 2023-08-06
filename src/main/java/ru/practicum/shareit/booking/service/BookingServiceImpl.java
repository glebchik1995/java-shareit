package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.BookingStatus.*;
import static ru.practicum.shareit.util.Constant.SORT_BY_START_DATE_DESC;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(BookingDtoShort bookingDtoShort, Long userId) {
        User user = isExistUser(userId);
        Item item = isExistItem(bookingDtoShort.getItemId());
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException("В бронировании отказано. Нельзя бронировать свою вещь");
        }
        if (!item.getAvailable()) {
            throw new ItemAlreadyBookedException("Данная вещь уже забронирована");
        }
        if (bookingDtoShort.getEnd().isBefore(bookingDtoShort.getStart()) ||
                bookingDtoShort.getEnd().isEqual(bookingDtoShort.getStart())) {
            throw new NotValidDateException("Дата и время начала должно быть раньше времени окончания");
        }
        Booking booking = BookingMapper.toBooking(bookingDtoShort, user, item);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        isExistUser(userId);
        Booking booking = isExistBooking(bookingId);
        if (booking.getStatus() != WAITING) {
            throw new BookingStatusAlreadyChangedException("Статус бронирования уже установлен: " + booking.getStatus());
        }
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new DataNotFoundException("Не подходящий id пользователя: " + userId);
        }
        booking.setStatus(approved ? APPROVED : REJECTED);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto findBookingByUser(Long userId, Long bookingId) {
        isExistUser(userId);
        Booking booking = isExistBooking(bookingId);
        if (!Objects.equals(booking.getBooker().getId(), userId)
                && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new DataNotFoundException("Не подходящий id пользователя: " + userId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> findAllBookingsCustomer(Long userId, BookingState state, Integer from, Integer size) {
        isExistUser(userId);
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from / size, size, SORT_BY_START_DATE_DESC);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now(), page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(userId, WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(userId, REJECTED, page);
                break;
            default:
                throw new NoSuchStateException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> findAllBookingsOwner(Long ownerId, BookingState state, Integer from, Integer size) {
        User owner = isExistUser(ownerId);
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from / size, size, SORT_BY_START_DATE_DESC);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwner(owner, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(
                        owner, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerAndEndBefore(owner, LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerAndStartAfter(owner, LocalDateTime.now(), page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerAndStatusEquals(owner, WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerAndStatusEquals(owner, REJECTED, page);
                break;
            default:
                throw new NoSuchStateException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    private User isExistUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
    }

    private Item isExistItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с ID = %d не найдена", itemId)));
    }

    private Booking isExistBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Бронирования с ID = %s не существует", bookingId)));
    }
}
