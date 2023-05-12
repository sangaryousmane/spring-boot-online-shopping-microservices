package com.example.ordermicroservice.exceptions;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CustomFeignErrorResponseException
        extends RuntimeException {

    private String errCode;
    private Integer status;

    public CustomFeignErrorResponseException(String message, String errCode, Integer status) {
        super(message);
        this.errCode = errCode;
        this.status = status;
    }
}
