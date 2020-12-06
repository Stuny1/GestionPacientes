package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(unique = true, nullable = false, length = 10)
    private String idDocument;
    @Column(nullable = false, length = 30)
    private String email;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_picture_route")
    private String profilePictureRoute;

    @OneToOne
    @JoinColumn(name = "name_id", referencedColumnName = "id")
    private Name name;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    public Person(
            String idDocument, String email,
            String firstName, String middleName, String lastName1, String lastName2
    ) {
        this.idDocument = idDocument;
        this.email = email;
        this.name = new Name(firstName, middleName, lastName1, lastName2);
    }

    public static class CompactPersonSerializer extends StdSerializer<Person> {

        public CompactPersonSerializer() {
            this(null);
        }

        public CompactPersonSerializer(Class<Person> t) {
            super(t);
        }

        @Override
        public void serialize(Person person, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("id", person.id);
            gen.writeStringField("idDocument", person.idDocument);
            gen.writeStringField("email", person.email);
            gen.writeObjectField("name", person.name);
            gen.writeEndObject();
        }
    }

}
