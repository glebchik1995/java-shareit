package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constant.SORT_BY_CREATED_DESC;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addItemRequest(Long userId, ItemRequestShortDto shortRequestDto) {
        User user = isExistUser(userId);
        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequestModel(shortRequestDto, user, now);
        itemRequest = requestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> findAllOwnItemRequest(Long userId) {
        isExistUser(userId);
        List<ItemRequest> requests = requestRepository.findAllByRequesterId(userId, SORT_BY_CREATED_DESC);
        List<Item> items = itemRepository.findAllByItemRequestIn(requests);
        List<ItemRequestDto> answer = new ArrayList<>();
        for (ItemRequest request : requests) {
            answer.add(ItemRequestMapper.toItemRequestDto(request, items.stream()
                    .filter(item -> Objects.equals(item.getItemRequest().getId(), request.getId()))
                    .collect(Collectors.toList())));
        }
        return answer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> findAllItemRequests(Long userId, Integer from, Integer size) {
        isExistUser(userId);
        PageRequest page = PageRequest.of(from / size, size, SORT_BY_CREATED_DESC);
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNot(userId, page).toList();
        List<Item> items = itemRepository.findAllByItemRequestIn(requests);
        List<ItemRequestDto> answer = new ArrayList<>();
        for (ItemRequest request : requests) {
            answer.add(ItemRequestMapper.toItemRequestDto(request, items.stream()
                    .filter(item -> Objects.equals(item.getItemRequest().getId(), request.getId()))
                    .collect(Collectors.toList())));
        }
        return answer;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findRequestById(Long userId, Long requestId) {
        isExistUser(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Запрос с ID = %s не найден", userId)));
        List<Item> items = itemRepository.findAllByItemRequest(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    private User isExistUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
    }
}
