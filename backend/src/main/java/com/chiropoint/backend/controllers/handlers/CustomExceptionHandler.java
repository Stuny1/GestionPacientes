package com.chiropoint.backend.controllers.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = CustomHttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> httpClientErrorExceptionHandler(CustomHttpClientErrorException ex) {
        ErrorResponse response = ex.asErrorResponse();
        logger.info(ex.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "Unexpected Error!");

        logger.error(ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
