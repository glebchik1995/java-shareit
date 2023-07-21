package ru.practicum.shareit.util;

import org.springframework.data.domain.Sort;

public class Constant {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String TIME_PATTERN = "yyyy-MM-dd";
    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "start");
}
