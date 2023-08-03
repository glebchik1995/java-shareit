package ru.practicum.shareit.request.repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private RequestRepository itemRequestRepository;

    @Test
    void findAllByRequesterId() {
    }

    @Test
    void findAllByRequesterIdNot() {
    }
}
