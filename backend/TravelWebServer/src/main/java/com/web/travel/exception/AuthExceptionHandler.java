package com.web.travel.exception;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.response.MessageResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

@RestControllerAdvice
public class AuthExceptionHandler{
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleConstrainViolationException(ConstraintViolationException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(
                new MessageResponse(false, "Invalid information", null)
        );
    }
}
