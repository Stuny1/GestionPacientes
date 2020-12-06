package com.chiropoint.backend.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChronicCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "condition_name", unique = true, nullable = false, length = 30)
    private String name;

    @Column(length = 100)
    private String description;

    public ChronicCondition(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
