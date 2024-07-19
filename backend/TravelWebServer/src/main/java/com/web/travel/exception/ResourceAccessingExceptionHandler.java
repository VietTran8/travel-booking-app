package com.web.travel.exception;

import com.web.travel.dto.ResDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@RestControllerAdvice
public class ResourceAccessingExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ResourceAccessingExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("404 situation detected.");
        return new ResDTO(
                HttpServletResponse.SC_NOT_FOUND,
                false,
                "Resource not found",
                null
        );
    }
}
