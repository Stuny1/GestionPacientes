package com.chiropoint.backend.dto.request.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleAppointmentRequest {

    @JsonProperty(required = true)
    private Date dateTime;

}
