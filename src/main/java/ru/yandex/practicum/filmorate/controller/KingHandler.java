package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.ValidationException;


@RestControllerAdvice()
public class KingHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFound(final EntityNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(e.getClass()), e.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationExcep(final ValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(e.getClass()), e.getMessage()), HttpStatus.valueOf(500));
    }

}
