package com.example.ordermicroservice.exceptions;

import com.example.ordermicroservice.external.decoder.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GenericExceptionHandler
        extends ResponseEntityExceptionHandler {


    // TODO: use for order not found exception
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleOrdersException(OrderNotFoundException oe) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(oe.getMessage());
        errorMessage.setHttpStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // TODO: method use to handle feign related exceptions
    @ExceptionHandler(CustomFeignErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomFeignErrorResponseException cf) {
        ErrorResponse errorMessage = ErrorResponse.builder()
                .message(cf.getMessage())
                .errorCode(cf.getErrCode())
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(cf.getStatus()));
    }
}
