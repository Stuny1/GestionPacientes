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
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(unique = true, nullable = false, length = 12)
    private String phoneNumber;

    @Column(nullable = false)
    @Setter
    private boolean telegram;
    @Column(nullable = false)
    @Setter
    private boolean whatsApp;

    public Phone(String phoneNumber, boolean telegram, boolean whatsApp) {
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.whatsApp = whatsApp;
    }

}
