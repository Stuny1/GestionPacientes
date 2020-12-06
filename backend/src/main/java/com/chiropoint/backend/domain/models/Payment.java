package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int payment;

    @Column(unique = true, nullable = false, length = 50)
    private String transactionId;

    @Column(nullable = false)
    private Timestamp dateTime;

    @Column(nullable = false)
    private boolean voided;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    @JsonProperty("officeId")
    @JsonIdentityReference(alwaysAsId = true)
    private Office office;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @JsonProperty("patientId")
    @JsonIdentityReference(alwaysAsId = true)
    private Patient patient;

    public Payment(Office office, Patient patient, int payment, String transactionId, Timestamp dateTime) {
        this.office = office;
        this.patient = patient;

        this.payment = payment;
        this.transactionId = transactionId;
        this.dateTime = dateTime;
    }

}
