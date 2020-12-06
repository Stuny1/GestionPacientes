package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OfficeWorker extends Person {

    @Column(name = "worker_position", length = 20)
    private String position;

    @Column(columnDefinition = "bit(4)", nullable = false)
    private int role;

    @ManyToMany(mappedBy = "workers")
    @JsonProperty("officeIds")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Office> offices;

    @Transient
    private List<WorkerRole> roles;

    public OfficeWorker(
            String idDocument, String email,
            String firstName, String middleName, String lastName1, String lastName2,
            String position, int role
    ) {
        super(idDocument, email, firstName, middleName,lastName1, lastName2);

        this.position = position;
        setRole(role);

        this.offices = new LinkedList<>();
    }

    public OfficeWorker(
            String idDocument, String email,
            String firstName, String middleName, String lastName1, String lastName2,
            String position, Collection<WorkerRole> roles
    ) {
        super(idDocument, email, firstName, middleName,lastName1, lastName2);

        this.position = position;
        setRoles(roles);

        this.offices = new LinkedList<>();
    }

    public boolean worksInOffice(Integer officeId) {
        if (officeId == null) {
            return false;
        }

        for (Office office : offices) {
            if (office.getId() != null && office.getId().equals(officeId)) {
                return true;
            }
        }

        return false;
    }

    public List<WorkerRole> getRoles() {
        if (this.roles == null) {
            setRole(this.role);
        }

        return roles;
    }

    public void setRoles(Collection<WorkerRole> roles) {
        this.roles = new LinkedList<>(roles);
        this.role = WorkerRole.getComposedRole(roles);
    }

    public void setRole(int composedRole) {
        this.role = composedRole;
        this.roles = new LinkedList<>();

        for (WorkerRole role : WorkerRole.values()) {
            if (WorkerRole.hasRole(composedRole, role)) {
                this.roles.add(role);
            }
        }
    }

    public boolean hasRole(WorkerRole role) {
        return roles.contains(role);
    }

    @JsonIgnore
    public Set<String> getRoleLiterals() {
        return roles.stream().map(r -> r.literal).collect(Collectors.toSet());
    }

    @AllArgsConstructor
    @Getter
    public enum WorkerRole {
        CHIROPRACTIC(1, "CHIROPRACTIC"),
        ECONOMIC_ASSISTANT(2, "ECONOMIC_ASSISTANT"),
        PATIENTS_ASSISTANT(4, "PATIENTS_ASSISTANT"),
        STATISTICS_ASSISTANT(8, "STATS_ASSISTANT");

        private int binaryValue;
        private String literal;

        public static int getComposedRole(Collection<WorkerRole> roles) {
            int composedRole = 0;
            for (WorkerRole role : roles) {
                composedRole += role.binaryValue;
            }

            return composedRole;
        }

        public static boolean hasRole(int composedRole, WorkerRole role) {
            return (composedRole & role.binaryValue) == role.binaryValue;
        }

    }

}
