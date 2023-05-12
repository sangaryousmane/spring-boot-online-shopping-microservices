package com.example.ordermicroservice.external.decoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ErrorResponse {

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "errorCode")
    private String errorCode;

    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
