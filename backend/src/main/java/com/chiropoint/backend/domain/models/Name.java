package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "person_name")
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(nullable = false, length = 20)
    private String firstName;
    @Column(length = 20)
    private String middleName;
    @Column(nullable = false, length = 20)
    private String lastName1;
    @Column(length = 20)
    private String lastName2;

    public Name(String firstName, String middleName, String lastName1, String lastName2) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
    }

}
