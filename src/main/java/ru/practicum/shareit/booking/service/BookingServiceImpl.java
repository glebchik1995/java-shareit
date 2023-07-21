package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingsRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constant.SORT_BY_START_DATE_DESC;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final ModelMapperUtil mapper;
    private final UserRepository userRepository;
    private final BookingsRepository bookingsRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new DataNotFoundException(String.format("Вещь с ID = %d не найдена", bookingDto.getItemId())));
        Booking booking = mapper.map(bookingDto, Booking.class);

        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException("В бронировании отказано. Нельзя бронировать свою вещь.");
        }
        booking.setStatus(Status.WAITING);
        booking.setBooker(user);
        booking.setItem(item);

        if (!booking.getItem().getAvailable()) {
            throw new ItemAlreadyBookedException(String.format("Вещь %s уже забронирована", booking.getItem()));
        }
        if (booking.getEnd().isBefore(booking.getStart())
                || booking.getStart().equals(booking.getEnd())) {
            throw new NotValidDateException("Дата и время начала должно быть раньше времени окончания");
        }

        return mapper.map(bookingsRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findBookingByUserOwner(Long userId, Long bookingId) {
        Booking booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Бронирование с ID = %d не найдено", bookingId)));
        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();
        if (userId.equals(bookerId) || userId.equals(ownerId)) {
            return mapper.map(booking, BookingDto.class);
        }

        throw new DataAccessException("Доступ к текущему бронированию отсутствует");
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getByOwner(Long userId, State state) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с id = %d не найден ", userId)));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingsRepository.findAllByItem_OwnerOrderByStartDesc(owner, SORT_BY_START_DATE_DESC);
                break;
            case PAST:
                bookings = bookingsRepository.findAllByItem_OwnerAndEndBeforeOrderByStartDesc(owner, now, SORT_BY_START_DATE_DESC);
                break;
            case FUTURE:
                bookings = bookingsRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(owner, now, SORT_BY_START_DATE_DESC);
                break;
            case CURRENT:
                bookings = bookingsRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner, now, now, SORT_BY_START_DATE_DESC);
                break;
            case REJECTED:
                bookings = bookingsRepository.findAllByItem_OwnerEqualsAndStatusEqualsOrderByStartDesc(owner, Status.REJECTED, SORT_BY_START_DATE_DESC);
                break;
            case WAITING:
                bookings = bookingsRepository.findAllByItem_OwnerEqualsAndStatusEqualsOrderByStartDesc(owner, Status.WAITING, SORT_BY_START_DATE_DESC);
                break;
            default:
                throw new NoSuchStateException("Unknown state: UNSUPPORTED_STATUS");

        }
        return bookings.stream()
                .map(booking -> mapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findUserBookings(Long userId, State state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с id = %d не найден ", userId)));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingsRepository.findAllByBookerOrderByStartDesc(user, SORT_BY_START_DATE_DESC);
                break;
            case PAST:
                bookings = bookingsRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(user, now, SORT_BY_START_DATE_DESC);
                break;
            case FUTURE:
                bookings = bookingsRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(user, now, SORT_BY_START_DATE_DESC);
                break;
            case CURRENT:
                bookings = bookingsRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(user, now, now, SORT_BY_START_DATE_DESC);
                break;
            case REJECTED:
                bookings = bookingsRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, Status.REJECTED, SORT_BY_START_DATE_DESC);
                break;
            case WAITING:
                bookings = bookingsRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, Status.WAITING, SORT_BY_START_DATE_DESC);
                break;
            default:
                throw new NoSuchStateException("Unknown state: UNSUPPORTED_STATUS");

        }
        return bookings.stream()
                .map(booking -> mapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден ", userId)));
        Booking booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Бронирование с ID = %d не найдено", bookingId)));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new DataAccessException("Текущая вещь вам не принадлежит");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BookingStatusAlreadyChangedException("Статут уже обновлен");
        }
        if (!booking.getStatus().equals(Status.WAITING)
                || !booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingStatusAlreadyChangedException("Статус бронирования уже установлен: " + bookingId);
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return mapper.map(bookingsRepository.save(booking), BookingDto.class);
    }
}
