package com.chiropoint.backend.dto.response;

import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
public class LogicResponse<T> {

    private T body;

    @Nullable
    private String error;
    private int code;

    public LogicResponse(@NotNull T body) {
        this.body = body;
        this.code = 0;
    }

    public LogicResponse(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public LogicResponse(@Nullable T body, int code, String error) {
        this.body = body;

        this.code = code;
        this.error = error;
    }

}
