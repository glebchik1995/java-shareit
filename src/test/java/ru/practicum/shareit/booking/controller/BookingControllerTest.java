package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constant.USER_ID_HEADER;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookingControllerTest {
    @InjectMocks
    BookingController bookingController;
    @Mock
    BookingService bookingService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    MockMvc mvc;
    BookingDto bookingDto;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        ItemDto itemDto = new ItemDto(1L, "item", "description", true, 1L);

        UserDto userDto = new UserDto(1L, "user", "email@mail.eu");

        bookingDto = new BookingDto(
                1L,
                itemDto,
                userDto,
                LocalDateTime.of(2023, 12, 5, 1, 1),
                LocalDateTime.of(2023, 12, 7, 1, 1),
                BookingStatus.WAITING);
    }

    @Test
    void shouldSaveBookingTest() throws Exception {
        when(bookingService.addBooking(any(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void shouldFindBookingByIdTest() throws Exception {
        when(bookingService.findBookingByUser(anyLong(), any())).
                thenReturn(bookingDto);

        mvc.perform((get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindAllBookingsOwnerWithStateAllTest() throws Exception {
        when(bookingService.findAllBookingsOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform((get("/bookings/owner?from=2&size=2")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].start[0]", is(bookingDto.getStart().getYear())))
                .andExpect(jsonPath("$[0].start[1]", is(bookingDto.getStart().getMonthValue())))
                .andExpect(jsonPath("$[0].start[2]", is(bookingDto.getStart().getDayOfMonth())))
                .andExpect(jsonPath("$[0].end[0]", is(bookingDto.getEnd().getYear())))
                .andExpect(jsonPath("$[0].end[1]", is(bookingDto.getEnd().getMonthValue())))
                .andExpect(jsonPath("$[0].end[2]", is(bookingDto.getEnd().getDayOfMonth())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindAllBookingsCustomerWithStateRejectTest() throws Exception {
        bookingDto.setStatus(BookingStatus.REJECTED);
        when(bookingService.findAllBookingsCustomer(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform((get("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].start[0]", is(bookingDto.getStart().getYear())))
                .andExpect(jsonPath("$[0].start[1]", is(bookingDto.getStart().getMonthValue())))
                .andExpect(jsonPath("$[0].start[2]", is(bookingDto.getStart().getDayOfMonth())))
                .andExpect(jsonPath("$[0].end[0]", is(bookingDto.getEnd().getYear())))
                .andExpect(jsonPath("$[0].end[1]", is(bookingDto.getEnd().getMonthValue())))
                .andExpect(jsonPath("$[0].end[2]", is(bookingDto.getEnd().getDayOfMonth())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindAllBookingsCustomerWithStateFutureTest() throws Exception {
        bookingDto.setStart(LocalDateTime.of(2024, 12, 24, 12, 0));
        when(bookingService.findAllBookingsCustomer(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform((get("/bookings?state=FUTURE&from=2&size=2")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].start[0]", is(bookingDto.getStart().getYear())))
                .andExpect(jsonPath("$[0].start[1]", is(bookingDto.getStart().getMonthValue())))
                .andExpect(jsonPath("$[0].start[2]", is(bookingDto.getStart().getDayOfMonth())))
                .andExpect(jsonPath("$[0].end[0]", is(bookingDto.getEnd().getYear())))
                .andExpect(jsonPath("$[0].end[1]", is(bookingDto.getEnd().getMonthValue())))
                .andExpect(jsonPath("$[0].end[2]", is(bookingDto.getEnd().getDayOfMonth())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindAllBookingsCustomerWithStatePastTest() throws Exception {
        bookingDto.setStart(LocalDateTime.of(2019, 12, 24, 12, 0));
        when(bookingService.findAllBookingsCustomer(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform((get("/bookings?state=PAST&from=2&size=2")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].start[0]", is(bookingDto.getStart().getYear())))
                .andExpect(jsonPath("$[0].start[1]", is(bookingDto.getStart().getMonthValue())))
                .andExpect(jsonPath("$[0].start[2]", is(bookingDto.getStart().getDayOfMonth())))
                .andExpect(jsonPath("$[0].end[0]", is(bookingDto.getEnd().getYear())))
                .andExpect(jsonPath("$[0].end[1]", is(bookingDto.getEnd().getMonthValue())))
                .andExpect(jsonPath("$[0].end[2]", is(bookingDto.getEnd().getDayOfMonth())))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsOfCurrentUserWithStateAllTest() throws Exception {
        when(bookingService.findAllBookingsCustomer(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform((get("/bookings?&from=2&size=2")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].start[0]", is(bookingDto.getStart().getYear())))
                .andExpect(jsonPath("$[0].start[1]", is(bookingDto.getStart().getMonthValue())))
                .andExpect(jsonPath("$[0].start[2]", is(bookingDto.getStart().getDayOfMonth())))
                .andExpect(jsonPath("$[0].end[0]", is(bookingDto.getEnd().getYear())))
                .andExpect(jsonPath("$[0].end[1]", is(bookingDto.getEnd().getMonthValue())))
                .andExpect(jsonPath("$[0].end[2]", is(bookingDto.getEnd().getDayOfMonth())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateAvailableStatus() throws Exception {
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }
}
