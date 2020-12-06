package com.chiropoint.backend.dto.request.office;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticModifyRequest {

    @JsonProperty(required = true)
    private String operation;
    private HashMap<String, Object> data;

}
