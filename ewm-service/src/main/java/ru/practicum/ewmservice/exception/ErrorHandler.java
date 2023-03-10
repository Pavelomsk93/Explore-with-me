package ru.practicum.ewmservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.info("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                "400 - BAD_REQUEST",
                e.getMessage(),
                ErrorStatusEwm.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final AlreadyExistException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "409 - CONFLICT",
                e.getMessage(),
                ErrorStatusEwm.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ErrorResponse(
                "404 - NOT_FOUND",
                e.getMessage(),
                ErrorStatusEwm.NOT_FOUND);
    }
}
