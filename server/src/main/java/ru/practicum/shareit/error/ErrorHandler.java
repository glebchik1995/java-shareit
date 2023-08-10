
package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.*;

import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public ErrorResponse handleValidationException(Throwable e) {
        log.debug("ResponseStatus: BAD_REQUEST. Status code: 400. {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Validation error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final DataNotFoundException e) {
        log.debug("ResponseStatus: NOT_FOUND. Status code: 404 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is not found!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final DataAlreadyExistException e) {
        log.debug("ResponseStatus: CONFLICT. Status code: 409 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is already exist");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(final Throwable e) {
        log.debug("ResponseStatus: INTERNAL_SERVER_ERROR. Status code: 500 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Server error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataAccessException(final DataAccessException e) {
        log.debug("ResponseStatus: NOT_FOUND. Status code: 404 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "No Object access!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingAlreadyStatusChanged(final BookingStatusAlreadyChangedException e) {
        log.debug("ResponseStatus: BAD_REQUEST. Status code: 400. {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is already changed");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handeDateTimeException(final NotValidDateException e) {
        log.debug("ResponseStatus: BAD_REQUEST. Status code: 400. {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Date Error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handeItemAlreadyBookedException(final ItemAlreadyBookedException e) {
        log.debug("ResponseStatus: BAD_REQUEST. Status code: 400. {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Date Error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handeNoSuchStateException(final NoSuchStateException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), "Unknown state: UNSUPPORTED_STATUS");
    }

}
