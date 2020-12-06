package com.chiropoint.backend.dto.request.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentQueryRequest {

    @JsonProperty(required = true)
    private Integer officeId;

    private Date from;
    private Date to;

    private Integer chiropractorId;
    private Integer patientId;

}
