package ru.practicum.shareit.util;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Comparator;

public class Constant {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String TIME_PATTERN = "yyyy-MM-dd";
    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "start");

    public static final Comparator<Booking> orderByStartDateAsc = (x, y) -> {
        if (x.getStart().isAfter(y.getStart())) {
            return 1;
        } else if (x.getStart().isBefore(y.getStart())) {
            return -1;
        } else {
            return 0;
        }
    };

    public static final Comparator<Booking> orderByStartDateDesc = (x, y) -> {
        if (x.getStart().isAfter(y.getStart())) {
            return -1;
        } else if (x.getStart().isBefore(y.getStart())) {
            return 1;
        } else {
            return 0;
        }
    };
}
