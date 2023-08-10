package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(final Throwable e) {
        log.debug("ResponseStatus: INTERNAL_SERVER_ERROR. Status code: 500 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Server error!");
    }
}
