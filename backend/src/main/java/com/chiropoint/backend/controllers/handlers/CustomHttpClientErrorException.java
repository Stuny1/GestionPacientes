package com.chiropoint.backend.controllers.handlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class CustomHttpClientErrorException extends HttpClientErrorException {

    private int errorCode;

    public CustomHttpClientErrorException(HttpStatus statusCode, int errorCode, String statusText) {
        super(statusCode, statusText);

        this.errorCode = errorCode;
    }

    public ErrorResponse asErrorResponse() {
        return new ErrorResponse(getStatusCode(), errorCode, getStatusText());
    }
}
