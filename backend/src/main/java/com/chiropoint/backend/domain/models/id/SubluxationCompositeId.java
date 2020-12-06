package com.chiropoint.backend.domain.models.id;

import com.chiropoint.backend.domain.models.Appointment;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class SubluxationCompositeId implements Serializable {

    private Integer code;
    private Appointment appointment;

}
