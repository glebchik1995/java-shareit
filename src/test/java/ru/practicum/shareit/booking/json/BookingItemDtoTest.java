package ru.practicum.shareit.booking.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingItemDtoTest {
    @Autowired
    private JacksonTester<BookingItemDto> tester;

    @Test
    void bookingItemDtoTest() throws IOException {

        BookingItemDto bookingItemDto = BookingItemDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 12, 0))
                .end(LocalDateTime.of(2000, 2, 1, 12, 0))
                .item(null)
                .bookerId(null)
                .status(BookingStatus.WAITING)
                .build();

        JsonContent<BookingItemDto> jsonContent = tester.write(bookingItemDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("2000-01-01T12:00:00");

        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("2000-02-01T12:00:00");

        assertThat(jsonContent).extractingJsonPathStringValue("$.item")
                .isEqualTo(bookingItemDto.getItem());

        assertThat(jsonContent).extractingJsonPathStringValue("$.bookerId")
                .isEqualTo(bookingItemDto.getBookerId());

        assertThat(jsonContent).extractingJsonPathStringValue("$.status")
                .isEqualTo(String.valueOf(bookingItemDto.getStatus()));
    }
}
