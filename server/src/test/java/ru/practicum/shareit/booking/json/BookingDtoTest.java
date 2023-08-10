package ru.practicum.shareit.booking.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> tester;

    @Test
    void bookingAllFieldsDtoTest() throws IOException {

        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.of(2000, 1, 1, 12, 10, 10))
                .end(LocalDateTime.of(2000, 2, 1, 12, 10, 10))
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        JsonContent<BookingDto> jsonContent = tester.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("2000-01-01T12:10:10");

        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("2000-02-01T12:10:10");

        assertThat(jsonContent).extractingJsonPathStringValue("$.item")
                .isEqualTo(bookingDto.getItem());

        assertThat(jsonContent).extractingJsonPathStringValue("$.booker")
                .isEqualTo(bookingDto.getBooker());

        assertThat(jsonContent).extractingJsonPathStringValue("$.status")
                .isEqualTo(String.valueOf(bookingDto.getStatus()));
    }
}
