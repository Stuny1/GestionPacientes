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
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entity_settings_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private EntitySettings entitySettings;

    @Column(name = "settingKey", nullable = false, length = 50)
    private String key;
    @Column(name = "settingValue", length = 50)
    private String value;

    public Setting(EntitySettings settings, String key, String value) {
        this.entitySettings = settings;

        this.key = key;
        this.value = value;
    }

}
