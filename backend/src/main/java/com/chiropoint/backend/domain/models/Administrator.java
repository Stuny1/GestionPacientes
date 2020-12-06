package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Administrator extends Person {

    @Column(name = "worker_position", length = 20)
    private String position;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @JsonProperty("organizationId")
    @JsonIdentityReference(alwaysAsId = true)
    private Organization organization;

    public Administrator(
            String idDocument, String email,
            String firstName, String middleName, String lastName1, String lastName2,
            Organization organization, String position
    ) {
        super(idDocument, email, firstName, middleName,lastName1, lastName2);

        this.organization = organization;
        this.position = position;
    }

}
