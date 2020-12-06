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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(length = 50, nullable = false)
    private String street;
    @Column(nullable = false)
    private int streetNumber;

    @Column(length = 10)
    private String block;
    private Integer aptNumber;

    @Column(nullable = false)
    private int zipCode;

    @Column(length = 30, nullable = false)
    private String area;
    @Column(length = 30, nullable = false)
    private String state;
    @Column(length = 30, nullable = false)
    private String country;

    public Address(
            String street, int number, String block, Integer aptNumber,
            int zipCode, String area, String state, String country
    ) {
        this.street = street;
        this.streetNumber = number;

        this.block = block;
        this.aptNumber = aptNumber;

        this.zipCode = zipCode;

        this.area = area;
        this.state = state;
        this.country = country;
    }


    public String readableAddress() {
        return street + " " + streetNumber +
                (block != null ? (" " + block) : "") +
                (aptNumber != 0 ? (" " + aptNumber) : "") +
                ", " + area + ", " + state;
    }

}
