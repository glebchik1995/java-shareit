package ru.practicum.shareit.booking.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoShortTest {

    @Autowired
    private JacksonTester<BookingDtoShort> tester;

    @Test
    void bookingDtoShortTest() throws IOException {

        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .start(LocalDateTime.of(2000, 1, 1, 12, 10, 10))
                .end(LocalDateTime.of(2000, 2, 1, 12, 10, 10))
                .itemId(null)
                .build();

        JsonContent<BookingDtoShort> jsonContent = tester.write(bookingDtoShort);

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("2000-01-01T12:10:10");

        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("2000-02-01T12:10:10");

        assertThat(jsonContent).extractingJsonPathStringValue("$.itemId")
                .isEqualTo(bookingDtoShort.getItemId());

    }
}
