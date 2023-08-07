package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;
    private Item item;
    private ItemDto itemDto;
    private ItemResponseDto itemResponseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .itemRequest(null)
                .build();

        itemDto = ItemMapper.toItemDto(item);

        itemResponseDto = ItemMapper.toItemDtoWithBooking(item, null, null, Collections.emptyList());

    }

    @Test
    void shouldAddItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        ItemDto result = itemService.addItem(1L, itemDto);
        assertNotNull(result);
        assertEquals(itemDto, result);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldUpdateItem() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        Item newItem = new Item(2L, "item", "best", true, user, null);
        ItemDto itemUpdate = new ItemDto(2L, "newName", "newDis", true, null);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(newItem));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(ItemMapper.toItemModel(itemUpdate, user));
        ItemDto result = itemService.updateItem(user.getId(), itemUpdate.getId(), itemUpdate);
        assertNotNull(result);
        assertEquals(itemUpdate, result);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldFindItemById() {

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));


        ItemResponseDto retrievedItem = itemService.findItemById(user.getId(), item.getOwner().getId());


        verify(itemRepository).findById(item.getId());

        assertEquals(item.getId(), retrievedItem.getId());
        assertEquals(item.getName(), retrievedItem.getName());
        assertEquals(item.getDescription(), retrievedItem.getDescription());
        assertEquals(item.getAvailable(), retrievedItem.getAvailable());
    }

    @Test
    void shouldFindAllItemsByUser() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        List<ItemResponseDto> result = itemService.findAllItemsByUser(1L, 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(itemResponseDto), result);
        verify(itemRepository, times(1)).findAllByOwnerId(anyLong(), any(Pageable.class));

    }

    @Test
    void shouldSearchByText() {
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDto> result = itemService.searchByText("item", 0, 20);
        assertNotNull(result);
        assertEquals(List.of(itemDto), result);
        verify(itemRepository, times(1))
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(),
                        any(Pageable.class));
    }

    @Test
    void shouldAddComment() {
        Comment comment = new Comment(1L, "text", item, user, null);
        CommentResponseDto commentDtoR = CommentMapper.toDtoResponse(comment);
        CommentDto commentDto = new CommentDto("text");
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItemIdAndBookerIdAndStatusAndStartBefore(anyLong(), anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        CommentResponseDto result = itemService.addComment(user.getId(), item.getId(), commentDto);
        assertNotNull(result);
        commentDtoR.setCreated(result.getCreated());
        assertEquals(commentDtoR, result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}