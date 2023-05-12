package com.example.productmicroservice.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlerProductNotFound(ProductNotFoundException pe) {
        ErrorMessage error=ErrorMessage.builder()
                .message(pe.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND).build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
