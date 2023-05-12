package com.example.ordermicroservice.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "httpStatus")
    private HttpStatus httpStatus;

    @JsonProperty(value = "errorCode")
    private String errorCode;

    public ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ErrorMessage(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
