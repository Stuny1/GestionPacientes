package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "archived = 0")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private AppointmentType type;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @JsonProperty("patient")
    @JsonSerialize(using = Person.CompactPersonSerializer.class)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "chiropractor_id", referencedColumnName = "id")
    @JsonProperty("chiropractor")
    @JsonSerialize(using = Person.CompactPersonSerializer.class)
    private OfficeWorker chiropractor;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    @JsonProperty("officeId")
    @JsonIdentityReference(alwaysAsId = true)
    private Office office;

    @OneToMany(targetEntity = Subluxation.class, mappedBy = "appointment")
    private List<Subluxation> subluxations;

    @Column(columnDefinition = "bit(3)",nullable = false)
    private Status status;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date previousDateTime;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private boolean archived;

    public Appointment(
            AppointmentType type, Date dateTime, Office office, Patient patient, OfficeWorker chiropractor
    ) {
        this.type = type;

        this.dateTime = dateTime;
        this.previousDateTime = dateTime;

        this.office = office;

        this.patient = patient;
        this.chiropractor = chiropractor;

        this.subluxations = new LinkedList<>();
        this.status = Status.REQUESTED;
        this.archived = false;
    }

    public enum Status {
        REQUESTED, PENDING, RESCHEDULED, IN_WAITING_ROOM, IN_PROGRESS, FINALIZED, CANCELLED;

        @JsonValue
        public int toValue() {
            return ordinal();
        }
    }

}
