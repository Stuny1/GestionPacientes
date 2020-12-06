package com.chiropoint.backend.domain.models;

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
@Table(name = "chiropoint_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 30)
    private String username;
    @Column(nullable = false, length = 50)
    private String password;

    @OneToOne(mappedBy = "user")
    private Person person;

    @OneToOne
    @JoinColumn(name = "settings_id", referencedColumnName = "id")
    private EntitySettings settings;

    @OneToMany(mappedBy = "user")
    private List<Permission> permissions;

    public User(Person person, String password) throws InstantiationException {
        if (person == null || person.getEmail() == null) {
            throw new InstantiationException("No person found, or the person doesn't have an email");
        }
        if (person.getUser() != null) {
            throw new InstantiationException("Person already has a username");
        }

        this.username = person.getEmail();
        this.password = password;

        this.settings = new EntitySettings();
        this.permissions = new LinkedList<>();
    }

    public boolean canAccessOffice(Integer officeId) {
        return person instanceof OfficeWorker && ((OfficeWorker) person).worksInOffice(officeId);
    }

    // TODO: changeSetting(), addPermission(), removePermission()

}
