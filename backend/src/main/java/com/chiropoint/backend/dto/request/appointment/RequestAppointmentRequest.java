package com.chiropoint.backend.dto.request.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAppointmentRequest {

    // TODO - remove this parameter once login is enabled
    @JsonProperty(required = true)
    private Integer patientId;

    @JsonProperty(required = true)
    private Integer typeId;

    @JsonProperty(required = true)
    private Date dateTime;

    @JsonProperty(required = true)
    private Integer officeId;
    @JsonProperty(required = true)
    private Integer chiropractorId;

}
