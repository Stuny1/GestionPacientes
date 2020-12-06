package com.chiropoint.backend.dto.request.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAppointmentRequest {

    private String description;

    @JsonProperty(required = true)
    private List<SubluxationDto> subluxations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubluxationDto {
        @JsonProperty(required = true)
        private char zone;
        @JsonProperty(required = true)
        private int vertebra;
        @JsonProperty(required = true)
        private int severity;
    }

}
