package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.exceptions.NotValidDateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.util.Constant.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = isExistUser(userId);
        Item item = ItemMapper.toItemModel(itemDto, user);
        if (itemDto.getRequestId() != null) {
            item.setItemRequest(requestRepository.findById(itemDto.getRequestId()).orElse(null));
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        isExistUser(userId);
        Item item = isExistItem(itemId);
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemResponseDto findItemById(Long userId, Long itemId) {
        isExistUser(userId);
        Item item = isExistItem(itemId);
        ItemResponseDto itemDto = getItemDtoWithBooking(item);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemResponseDto> findAllItemsByUser(Long userId, Integer from, Integer size) {
        isExistUser(userId);
        PageRequest page = PageRequest.of(from / size, size);
        Page<Item> items = itemRepository.findAllByOwnerId(userId, page);
        List<ItemResponseDto> list = new ArrayList<>();
        for (Item it : items) {
            list.add(getItemDtoWithBooking(it));
        }
        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchByText(String text, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (!StringUtils.hasLength(text)) {
            return Collections.emptyList();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, page).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = isExistUser(userId);
        Item item = isExistItem(itemId);
        Collection<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndStatusAndStartBefore(itemId,
                userId, APPROVED, LocalDateTime.now());
        if (bookings == null) {
            throw new NotValidDateException("Бронирований не найдено");
        }
        if (bookings.isEmpty()) {
            throw new ValidationException("Данная вещь не была арендована.");
        }
        Comment comment = CommentMapper.toComment(commentDto, user, item, LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.toDtoResponse(comment);
    }

    private ItemResponseDto getItemDtoWithBooking(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Collection<BookingItemDto> bookings = bookingRepository.findAllByItem(item, SORT_BY_START_DATE_DESC)
                .stream().map(BookingMapper::toBookingDtoItem).collect(Collectors.toList());
        BookingItemDto last = bookings.stream()
                .sorted(orderByStartDateDesc)
                .filter(b -> b.getStart().isBefore(now))
                .findFirst()
                .orElse(null);
        BookingItemDto next = bookings.stream()
                .sorted(orderByStartDateAsc)
                .filter(b -> b.getStart().isAfter(now) && b.getStatus() == APPROVED)
                .findFirst()
                .orElse(null);
        List<CommentResponseDto> comments = commentRepository.findAllByItemId(item.getId()).stream()
                .map(CommentMapper::toDtoResponse)
                .collect(Collectors.toList());
        return ItemMapper.toItemDtoWithBooking(item, next, last, comments);
    }

    private User isExistUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
    }

    private Item isExistItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new DataNotFoundException(String.format("Вещь с ID = %d не найдена", itemId)));
    }
}



