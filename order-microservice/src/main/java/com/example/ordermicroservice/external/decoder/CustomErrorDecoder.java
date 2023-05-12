package com.example.ordermicroservice.external.decoder;

import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {


    // TODO: error decoder for the product service
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());
        log.info("::{}", response.status());

        try {
            ErrorResponse errorMessage = objectMapper.readValue(
                    response.body().asInputStream(),
                    ErrorResponse.class);
            return new CustomFeignErrorResponseException(
                    errorMessage.getMessage(),
                    errorMessage.getErrorCode(),
                    response.status());
        } catch (IOException oe) {
            throw new CustomFeignErrorResponseException(
                    "Product doesn't have sufficient quantity ", " INTERNAL_SERVER_ERROR ", 500);
        }
    }
}
