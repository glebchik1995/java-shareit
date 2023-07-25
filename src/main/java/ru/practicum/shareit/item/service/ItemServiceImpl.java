
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
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.ModelMapperUtil;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constant.orderByStartDateAsc;
import static ru.practicum.shareit.util.Constant.orderByStartDateDesc;

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
        List<Item> items = itemRepository.findItemByText(text);
        return findItemsDto(items, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoWithBookings findItemById(Long userId, Long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Вещь с ID = %d не найдена", itemId)));

        return findItemsDto(Collections.singletonList(item), userId).get(0);
    }

    private List<ItemDtoWithBookings> findItemsDto(List<Item> items, long userId) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> bookings;

        if (items.size() == 1) {
            bookings = bookingsRepository.findBookingsByItemId(items, userId, Status.APPROVED);
        } else {
            bookings = bookingsRepository.findBookingsByItemIn(items);
        }

        List<Comment> comments = commentRepository.findCommentsByItemInOrderByCreated(items);

        Map<Item, List<Booking>> bookingsMap = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getItem));

        Map<Item, List<Comment>> commentsMap = comments.stream()
                .collect(Collectors.groupingBy(Comment::getItem));

        return items.stream()
                .map(item -> {
                    List<CommentDto> itemComments = commentsMap
                            .getOrDefault(item, Collections.emptyList())
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());

                    List<Booking> itemBookings = bookingsMap.getOrDefault(item, Collections.emptyList());

                    Optional<Booking> lastOptional = getLastItem(itemBookings);
                    Optional<Booking> nextOptional = getNextItem(itemBookings);

                    ResponseBookingDto last = lastOptional
                            .map(BookingMapper::toResponseBookingDto)
                            .orElse(null);

                    ResponseBookingDto next = nextOptional
                            .map(BookingMapper::toResponseBookingDto).orElse(null);

                    return ItemMapper.toItemDtoWithBookings(item, last, next, itemComments);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoWithBookings> findAllItems(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return findItemsDto(items, userId);
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

    private Optional<Booking> getNextItem(List<Booking> bookings) {
        LocalDateTime currentTime = LocalDateTime.now();
        return bookings.stream()
                .sorted(orderByStartDateAsc)
                .filter(t -> t.getStart().isAfter(currentTime) &&
                        t.getStatus().equals(Status.APPROVED))
                .findFirst();
    }

    private Optional<Booking> getLastItem(List<Booking> bookings) {
        LocalDateTime currentTime = LocalDateTime.now();
        return bookings.stream()
                .sorted(orderByStartDateDesc)
                .filter(t -> t.getStart().isBefore(currentTime) &&
                        t.getStatus().equals(Status.APPROVED))
                .findFirst();
    }
}


