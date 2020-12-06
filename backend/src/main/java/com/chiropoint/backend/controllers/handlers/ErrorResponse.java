package com.chiropoint.backend.controllers.handlers;

import com.chiropoint.backend.dto.response.LogicResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Data
@AllArgsConstructor
@JsonSerialize(using = ErrorResponse.ErrorResponseSerializer.class)
public class ErrorResponse {

    private HttpStatus status;

    private int code;
    private String error;

    public ResponseEntity<ErrorResponse> asResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }

    public static ErrorResponse of(LogicResponse response) {

        int statusCode = response.getCode() >= 400 && response.getCode() < 600 ? response.getCode() / 100 : 400;

        return new ErrorResponse(HttpStatus.resolve(statusCode), response.getCode(), response.getError());
    }

    public static ErrorResponse unauthorized(int code, String message) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, code, message);
    }

    public static ErrorResponse notFound(int code, String message) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, code, message);
    }

    public static ErrorResponse generic404() {
        return notFound(40400, "Not found");
    }

    public static class ErrorResponseSerializer extends StdSerializer<ErrorResponse> {

        public ErrorResponseSerializer() {
            this(null);
        }

        public ErrorResponseSerializer(Class<ErrorResponse> t) {
            super(t);
        }

        @Override
        public void serialize(ErrorResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("statusCode", value.status.value());
            gen.writeNumberField("code", value.code);
            gen.writeStringField("status", value.status.getReasonPhrase());
            gen.writeStringField("error", value.error);
            gen.writeEndObject();
        }
    }
}
