package ru.practicum.shareit.item.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Data
public class ItemWithBookingsDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private ResponseBookingDto lastBooking;

    private ResponseBookingDto nextBooking;

    private List<CommentDto> comments;
}
