package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.info("Ошибка валидации {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Validation error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final DataNotFoundException e) {
        log.info("Ошибка 404 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is not found!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(final HttpServerErrorException e) {
        log.info("Ошибка 500 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Unknown error!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final DataAlreadyExistException e) {
        log.info("Ошибка 409 {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "Object is already exist");
    }
}
