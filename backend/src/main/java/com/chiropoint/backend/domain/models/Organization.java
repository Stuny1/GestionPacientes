package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_name", unique = true, nullable = false, length = 20)
    private String name;

    @OneToOne
    @JoinColumn(name = "settings_id", referencedColumnName = "id")
    @JsonIgnore
    private EntitySettings settings;

    public Organization(String name) {
        this.name = name;

        this.settings = new EntitySettings();
    }

    // TODO: changeSetting()

}
