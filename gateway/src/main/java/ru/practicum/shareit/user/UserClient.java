package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import static ru.practicum.shareit.util.Constant.API_PREFIX_FOR_USERS;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX_FOR_USERS))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> findUserById(Long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto, Long userId) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> deleteUserById(Long userId) {
        return delete("/" + userId);
    }
}
