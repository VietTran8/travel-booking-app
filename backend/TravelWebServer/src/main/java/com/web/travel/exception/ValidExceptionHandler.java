package com.web.travel.exception;

import com.web.travel.payload.response.AuthResponse;
import com.web.travel.payload.response.MessageResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> methodArgumentNotValidExceptionHandler(){
        return ResponseEntity.badRequest().body(
                new MessageResponse(false, "Invalid information", null)
        );
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<AuthResponse> fileSizeLimitExceededExceptionHandler(){
        return ResponseEntity.badRequest().body(
                new AuthResponse(HttpServletResponse.SC_BAD_REQUEST, "The field file exceeds its maximum permitted size of 10 MB")
        );
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<AuthResponse> sizeLimitExceededExceptionHandler(){
        return ResponseEntity.badRequest().body(
                new AuthResponse(HttpServletResponse.SC_BAD_REQUEST, "The field file exceeds its maximum permitted size of 10 MB")
        );
    }
}
