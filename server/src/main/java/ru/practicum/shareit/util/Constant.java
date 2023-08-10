package ru.practicum.shareit.util;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.Comparator;

public class Constant {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DEFAULT_FROM_VALUE = "0";
    public static final String DEFAULT_SIZE_VALUE = "20";
    public static final Sort SORT_BY_START_DATE_DESC = Sort.by("start").descending();
    public static final Sort SORT_BY_CREATED_DESC = Sort.by("created").descending();

    public static final Comparator<BookingItemDto> orderByStartDateAsc = (x, y) -> {
        if (x.getStart().isAfter(y.getStart())) {
            return 1;
        } else if (x.getStart().isBefore(y.getStart())) {
            return -1;
        } else {
            return 0;
        }
    };

    public static final Comparator<BookingItemDto> orderByStartDateDesc = (x, y) -> {
        if (x.getStart().isAfter(y.getStart())) {
            return -1;
        } else if (x.getStart().isBefore(y.getStart())) {
            return 1;
        } else {
            return 0;
        }
    };
}

