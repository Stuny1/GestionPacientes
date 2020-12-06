package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Patient extends Person {

    @Column(nullable = false)
    private int balance;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private Phone phone;

    @ManyToMany
    @JoinTable(name="patient_conditions")
    private List<ChronicCondition> chronicConditions;

    @OneToMany(mappedBy = "patient")
    @JsonProperty("appointmentIds")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    @JsonProperty("paymentIds")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Payment> payments;

    public Patient(
            String idDocument, String email,
            String firstName, String middleName, String lastName1, String lastName2,
            Address address, Phone phone
    ) {
        super(idDocument, email, firstName, middleName, lastName1, lastName2);

        this.phone = phone;
        this.address = address;
        this.chronicConditions = new LinkedList<>();
        this.appointments = new LinkedList<>();
    }

}
