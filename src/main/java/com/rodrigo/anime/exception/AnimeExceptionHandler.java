package com.rodrigo.anime.exception;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.rodrigo.anime.validation.ValidationErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class AnimeExceptionHandler {

    @ExceptionHandler(AnimeNotFoundException.class)
    public ResponseEntity<?> handlerAnimeNotFoundException(AnimeNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handlerConstraintViolationException(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Set<String> mensagens = new HashSet<>(constraintViolations.size());
        mensagens.addAll(constraintViolations.stream()
                                                .map(ConstraintViolation::getMessage)
                                                .collect(Collectors.toList()));

        return new ResponseEntity<>(mensagens, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException (MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(","));

        String fieldMessages = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));

        ValidationErrorDetails veDetails = ValidationErrorDetails.Builder.newBuilder().timestamp(LocalDate.now())
                .title("Erros de validações.")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Erros de validações.")
                .developerMessage(e.getClass()
                .getName()).field(fields)
                .fieldMessage(fieldMessages)
                .build();

        return new ResponseEntity<>(veDetails, HttpStatus.BAD_REQUEST);
    }

}
