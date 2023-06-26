package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public ErrorResponse handleValidationException(Throwable e) {
        log.debug("ResponseStatus: BAD_REQUEST. Status code: 400. {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Validation error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final DataNotFoundException e) {
        log.debug("ResponseStatus: NOT_FOUND. Status code: 404 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is not found!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(final Throwable e) {
        log.debug("ResponseStatus: INTERNAL_SERVER_ERROR. Status code: 500 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Server error!");
    }

    @ExceptionHandler({DataAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final RuntimeException e) {
        log.debug("ResponseStatus: CONFLICT. Status code: 409 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is already exist");
    }
}
