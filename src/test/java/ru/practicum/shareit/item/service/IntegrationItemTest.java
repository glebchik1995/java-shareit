package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemTest {

    private final EntityManager entityManager;

    private final ItemService itemService;

    private final UserService userService;
    public static UserDto userDto;
    public ItemDto itemDto;

    @BeforeEach
    void setUp() {

        userDto = UserDto.builder()
                .name("name")
                .email("practikum@email.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    @DirtiesContext
    void shouldAddItemTest() {
        UserDto owner = userService.addUser(userDto);

        ItemDto newItemDto = itemService.addItem(owner.getId(), itemDto);

        assertThat(newItemDto.getName(), equalTo(itemDto.getName()));
        assertThrows(DataNotFoundException.class,
                () -> itemService.addItem(100L, itemDto));
    }

    @Test
    @DirtiesContext
    void shouldFindItemByIdTest() {
        UserDto savedUser = userService.addUser(userDto);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemResponseDto gotItem = itemService.findItemById(savedUser.getId(), savedItem.getId());
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(savedItem.getName()));
        assertThat(gotItem.getAvailable(), equalTo(savedItem.getAvailable()));
        userService.deleteUserById(savedUser.getId());
    }

    @Test
    @DirtiesContext
    void shouldUpdateItem() {
        UserDto owner = userService.addUser(userDto);
        itemService.addItem(owner.getId(), itemDto);

        ItemDto updateItemDto = ItemDto.builder()
                .name("newName")
                .description("newDescription")
                .available(false)
                .build();

        itemService.updateItem(1L, 1L, updateItemDto);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", 1L).getSingleResult();

        assertThat(updateItemDto.getName(), equalTo(item.getName()));
        assertThat(updateItemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(updateItemDto.getAvailable(), equalTo(item.getAvailable()));
        assertNull(itemDto.getRequestId());
    }

    @Test
    @DirtiesContext
    void shouldFindAllByUserId() {
        UserDto owner = userService.addUser(userDto);
        itemService.addItem(owner.getId(), itemDto);
        List<ItemResponseDto> items = itemService.findAllItemsByUser(owner.getId(), 0, 20);
        assertThat(items.size(), equalTo(1));
    }
}


