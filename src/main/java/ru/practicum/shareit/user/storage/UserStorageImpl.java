package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
@Slf4j
public class UserStorageImpl implements UserStorage {

    private long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }
}
