package com.web.travel.exception;

import com.web.travel.dto.ResDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class FileUploadExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResDTO> IOExceptionHandler(IOException e){
        return ResponseEntity.badRequest().body(
                new ResDTO(HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        e.getMessage(),
                        null)
        );
    }
}
