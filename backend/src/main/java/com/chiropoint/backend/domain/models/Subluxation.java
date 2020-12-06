package com.chiropoint.backend.domain.models;

import com.chiropoint.backend.domain.models.id.SubluxationCompositeId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(value = SubluxationCompositeId.class)
public class Subluxation {

    @Id
    @Column(columnDefinition = "bit(8)")
    @JsonIgnore
    private Integer code;

    @Id
    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Appointment appointment;

    @Transient
    private char zone;
    @Transient
    private int vertebra;
    @Transient
    private int severity;

    public Subluxation(int code) throws InstantiationException {
        this.code = code;

        this.zone = getZoneChar((code & 0b110000) >> 6);
        this.vertebra = ((code & 0b1100) >> 2) + 1;
        this.severity = code & 0b11;

        if (!checkVertebraValidity(zone, vertebra)) {
            throw new InstantiationException("Could not parse code. Invalid zone-vertebra combination.");
        }
    }

    public Subluxation(char zone, int vertebra, int severity) throws InstantiationException {
        int zoneBit = getZoneBit(zone);
        if (zoneBit == -1 || !checkVertebraValidity(zone, vertebra) || severity < 0 || severity > 3) {
            throw new InstantiationException("Invalid zone code");
        }

        this.zone = zone;
        this.vertebra = vertebra;
        this.severity = severity;

        this.code =  (zoneBit << 6) + ((vertebra - 1) << 2) + severity;
    }

    private int getZoneBit(char zone) {
        switch (zone) {
            case 'C':
                return 0;
            case 'D':
                return 1;
            case 'L':
                return 2;
            case 'S':
                return 3;
            default:
                return -1;
        }
    }

    private char getZoneChar(int zoneBit) {
        switch (zoneBit) {
            case 0:
                return 'C';
            case 1:
                return 'D';
            case 2:
                return 'L';
            case 3:
                return 'S';
            default:
                return 'N';
        }
    }

    private boolean checkVertebraValidity(char zone, int number) {
        if (number <= 0) {
            return false;
        }

        switch (zone) {
            case 'C':
                return number <= 7;
            case 'D':
                return number <= 12;
            case 'L':
                return number <= 5;
            case 'S':
                return number <= 2;
            default:
                return false;
        }
    }

}
