package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;


@Repository
@Slf4j
public class UserStorageImpl implements UserStorage {

    private long id = 0L;
    private final Map<Long, User> users = new HashMap<>();


    @Override
    public User create(User user) {
        if (findAll().stream().anyMatch(s -> s.getEmail().equals(user.getEmail()))) {
            throw new DataAlreadyExistException("Пользователь с таким email уже существует");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь: {} создан", user.getName());
        return user;
    }

    @Override
    public User findById(Long id) {
        log.info("Пользователь с ID = {} получен", id);
        return users.values()
                .stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %d не найден", id)));
    }

    @Override
    public List<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user) {

        User updateUser = findById(user.getId());

        Set<String> emails = users.values().stream().map(User::getEmail).collect(Collectors.toSet());

        if (emails.contains(user.getEmail()) && (!user.getEmail().equals(updateUser.getEmail()))) {
            throw new DataAlreadyExistException(("Email занят другим пользователем"));
        }
        if (user.getEmail() != null && !updateUser.getEmail().isBlank()) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !updateUser.getName().isBlank()) {
            updateUser.setName(user.getName());
        }
        users.put(updateUser.getId(), updateUser);
        return updateUser;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
        log.info("Пользователь с ID: {} удален", id);
    }
}
