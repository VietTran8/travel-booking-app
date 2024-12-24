package com.web.travel.exception;

import com.web.travel.dto.ResDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResDTO> notFoundExceptionHandler(NotFoundException exception) {
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                exception.getMessage(),
                null
        ));
    }
}
