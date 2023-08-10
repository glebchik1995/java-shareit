package ru.practicum.shareit.handler;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.exceptions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ErrorHandlerTest {
    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleAlreadyExistTest() {
        ErrorResponse exceptionMessage = errorHandler.handleAlreadyExistException(new DataAlreadyExistException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handleNotFoundExceptionTest() {
        ErrorResponse exceptionMessage = errorHandler.handleNotFoundException(new DataNotFoundException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handleIllegalArgumentException() {
        ErrorResponse exceptionMessage = errorHandler.handleIllegalArgumentException(new Throwable("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handleDataAccessException() {
        ErrorResponse exceptionMessage = errorHandler.handleDataAccessException(new DataAccessException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }


    @Test
    void validationExceptionTest() {
        ErrorResponse exceptionMessage = errorHandler.handleValidationException(new Throwable("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handleBookingAlreadyStatusChangedTest() {
        ErrorResponse exceptionMessage = errorHandler.handleBookingAlreadyStatusChanged(new BookingStatusAlreadyChangedException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handeDateTimeExceptionTest() {
        ErrorResponse exceptionMessage = errorHandler.handeDateTimeException(new NotValidDateException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handeItemAlreadyBookedExceptionTest() {
        ErrorResponse exceptionMessage = errorHandler.handeItemAlreadyBookedException(new ItemAlreadyBookedException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }

    @Test
    void handeNoSuchStateExceptionTest() {
        ErrorResponse exceptionMessage = errorHandler.handeNoSuchStateException(new NoSuchStateException("message"));
        assertThat(exceptionMessage.getError(), equalTo("message"));
    }
}

