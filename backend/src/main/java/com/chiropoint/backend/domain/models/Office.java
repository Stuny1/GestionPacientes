package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.*;
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
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "office_name", nullable = false, length = 50)
    private String name;

    @OneToOne
    @JoinColumn(name = "settings_id", referencedColumnName = "id")
    @JsonIgnore
    private EntitySettings settings;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private Phone phone;

    @OneToMany(mappedBy = "office")
    private List<Statistic> statistics;

    @ManyToMany
    @JoinTable(
            name = "office_workers",
            joinColumns = @JoinColumn(name = "office_id"),
            inverseJoinColumns = @JoinColumn(name = "worker_id")
    )
    @JsonProperty("workerIds")
    @JsonIdentityReference(alwaysAsId = true)
    private List<OfficeWorker> workers;

    public Office(Organization organization, String name, Address address, Phone phone) {
        this.organization = organization;

        this.name = name;

        this.address = address;
        this.phone = phone;

        this.settings = new EntitySettings();
        this.workers = new LinkedList<>();
    }

    public Office(Organization organization, String name) {
        this(organization, name, null, null);
    }

    // TODO: changeSetting()

}
