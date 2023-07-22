
package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingsRepository;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ModelMapperUtil mapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingsRepository bookingsRepository;

    @Override
    public ItemDto addItemById(ItemDto itemDto, Long userId) {
        Item item = mapper.map(itemDto, Item.class);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId))));
        item.setAvailable(true);
        return mapper.map(itemRepository.save(item), ItemDto.class);
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с ID = %d не найдена", itemId)));
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId));
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return mapper.map(itemRepository.save(item), ItemDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoWithBookings> searchByText(Long userId, String text) {
        userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("Пользователь не найден")
        );

        if (text.isBlank()) {
            return Collections.emptyList();
        }
        itemRepository.findItemByText(text);

        List<ItemDtoWithBookings> items = itemRepository.findItemByText(text)
                .stream()
                .map(item -> mapper.map(item, ItemDtoWithBookings.class))
                .collect(Collectors.toList());

        return findAllItemsWithBookings(items);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoWithBookings findItemById(Long userId, Long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с ID = %d не найдена", itemId)));

        ItemDtoWithBookings itemDtoWithBookings = mapper.map(item, ItemDtoWithBookings.class);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            ResponseBookingDto lastBooking = BookingMapper.toResponseBookingDto(bookingsRepository
                    .findBookingByItemIdAndStartBeforeOrderByEndDesc(itemId, LocalDateTime.now())
                    .stream().findFirst().orElse(null));
            itemDtoWithBookings.setLastBooking(lastBooking);

            ResponseBookingDto nextBooking = BookingMapper.toResponseBookingDto(bookingsRepository
                    .findBookingByItem_IdAndStartAfterAndStatusEqualsOrderByStart(itemId, LocalDateTime.now(), Status.APPROVED)
                    .stream().findFirst().orElse(null));
            itemDtoWithBookings.setNextBooking(nextBooking);
        }
        itemDtoWithBookings.setComments(new ArrayList<>());
        itemDtoWithBookings.getComments().addAll(findAllComments(itemId));
        return itemDtoWithBookings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoWithBookings> findAllItems(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        List<ItemDtoWithBookings> items = itemRepository.findAllByOwnerOrderById(owner)
                .stream()
                .map(item -> mapper.map(item, ItemDtoWithBookings.class))
                .collect(Collectors.toList());
        return findAllItemsWithBookings(items);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setCreated(LocalDateTime.now());
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с ID = %d не найден", itemId)));
        item.getComments().add(commentDto);
        comment.setItem(item);
        comment.setAuthor(userRepository.findById(userId).orElseThrow(() ->
                new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId))));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = bookingsRepository
                .findBookingsByBooker_IdAndItem_IdAndStatusEqualsAndStartBeforeAndEndBefore(
                        userId, itemId, Status.APPROVED, now, now
                );

        if (bookings.isEmpty()) {
            throw new ValidationException("Данная вещь не была арендована.");
        }
        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }

    private List<CommentDto> findAllComments(Long itemId) {
        return commentRepository
                .findCommentByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private List<ItemDtoWithBookings> findAllItemsWithBookings(List<ItemDtoWithBookings> list) {
        List<Booking> bookings = new ArrayList<>(bookingsRepository.findAll());
        for (ItemDtoWithBookings item : list) {
            ResponseBookingDto lastBooking = bookings
                    .stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId()) && booking.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getEnd))
                    .map(BookingMapper::toResponseBookingDto)
                    .findFirst()
                    .orElse(null);
            item.setLastBooking(lastBooking);

            ResponseBookingDto nextBooking = bookings
                    .stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId()) && booking.getStart()
                            .isAfter(LocalDateTime.now()) && booking.getStatus().equals(Status.APPROVED))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .map(BookingMapper::toResponseBookingDto)
                    .findFirst()
                    .orElse(null);
            item.setNextBooking(nextBooking);
        }
        return list;
    }

}


