package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingsRepository;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ModelMapperUtil mapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingsRepository bookingsRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = mapper.map(itemDto, Item.class);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с id = %d не найден", userId))));
        item.setAvailable(true);

        return mapper.map(itemRepository.save(item), ItemDto.class);
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с id = %d не найдена", itemId)));
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException(String.format("Пользователь с id = %d не найден", userId));
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
    public List<ItemDto> searchItemBySubstring(String text) {
        return itemRepository.findItemByText(text)
                .stream()
                .map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingsDto getItemById(Long userId, Long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с id = %d не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с id = %d", itemId)));

        ItemWithBookingsDto itemDtoWithBookings = mapper.map(item, ItemWithBookingsDto.class);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            ResponseBookingDto lastBooking = bookingMapper.toResponseBookingDto(bookingsRepository
                    .findBookingByItemIdAndStartBeforeOrderByEndDesc(itemId, LocalDateTime.now())
                    .stream().findFirst().orElse(null));
            itemDtoWithBookings.setLastBooking(lastBooking);

            ResponseBookingDto nextBooking = bookingMapper.toResponseBookingDto(bookingsRepository
                    .findBookingByItem_IdAndStartAfterAndStatusEqualsOrderByStart(itemId, LocalDateTime.now(), Status.APPROVED)
                    .stream().findFirst().orElse(null));
            itemDtoWithBookings.setNextBooking(nextBooking);
        }
        itemDtoWithBookings.setComments(new ArrayList<>());
        itemDtoWithBookings.getComments().addAll(getAllComments(itemId));
        return itemDtoWithBookings;
    }


    @Override
    public List<ItemWithBookingsDto> getAllItems(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с id = %d не найден", userId)));
        List<ItemWithBookingsDto> items = itemRepository.findAllByOwnerOrderById(owner)
                .stream()
                .map(item -> mapper.map(item, ItemWithBookingsDto.class))
                .collect(Collectors.toList());

        return getAllItemsWithBookings(items);
    }

    private List<ItemWithBookingsDto> getAllItemsWithBookings(List<ItemWithBookingsDto> list) {
        List<Booking> bookings = new ArrayList<>(bookingsRepository
                .findAll());
        for (ItemWithBookingsDto item : list) {
            ResponseBookingDto lastBooking = bookings
                    .stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId()) && booking.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getEnd))
                    .map(bookingMapper::toResponseBookingDto)
                    .findFirst()
                    .orElse(null);
            item.setLastBooking(lastBooking);

            ResponseBookingDto nextBooking = bookings
                    .stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId()) && booking.getStart()
                            .isAfter(LocalDateTime.now()) && booking.getStatus().equals(Status.APPROVED))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .map(bookingMapper::toResponseBookingDto)
                    .findFirst()
                    .orElse(null);
            item.setNextBooking(nextBooking);
        }
        return list;
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setCreated(LocalDateTime.now());
        Item item = itemRepository.findById(itemId).
                orElseThrow(() -> new DataNotFoundException(String.format("Вещь с id = %d не найден", itemId)));
        item.getComments().add(commentDto);
        comment.setItem(item);
        comment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с id = %d не найден", userId))));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = bookingsRepository.findBookingsByBooker_IdAndItem_IdAndStatusEqualsAndStartBeforeAndEndBefore(userId, itemId, Status.APPROVED, now, now);

        if (bookings.isEmpty()) {
            throw new ValidationException("Данная вещь не была арендована.");
        }
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    private List<CommentDto> getAllComments(Long itemId) {
        return commentRepository
                .findCommentByItemId(itemId)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
