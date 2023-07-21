package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingsRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_OwnerOrderByStartDesc(User owner, Sort sortByStartDateDesc);

    List<Booking> findAllByItem_OwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime now, Sort sortByStartDateDesc);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User owner, LocalDateTime now, Sort sortByStartDateDesc);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User owner, LocalDateTime start, LocalDateTime end, Sort sortByStartDateDesc);

    List<Booking> findAllByItem_OwnerEqualsAndStatusEqualsOrderByStartDesc(User owner, Status status, Sort sortByStartDateDesc);

    List<Booking> findBookingByItemIdAndStartBeforeOrderByEndDesc(Long itemId, LocalDateTime time);

    List<Booking> findBookingByItem_IdAndStartAfterAndStatusEqualsOrderByStart(Long itemId, LocalDateTime time, Status status);

    List<Booking> findBookingsByBooker_IdAndItem_IdAndStatusEqualsAndStartBeforeAndEndBefore(Long userId, Long itemId, Status status, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerOrderByStartDesc(User user, Sort sortByStartDateDesc);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now, Sort sortByStartDateDesc);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now, Sort sortByStartDateDesc);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User user, LocalDateTime start, LocalDateTime end, Sort sortByStartDateDesc);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, Status status, Sort sortByStartDateDesc);
}
