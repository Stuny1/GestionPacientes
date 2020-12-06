package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statistic_id_generator")
    @SequenceGenerator(name = "statistic_id_generator", sequenceName = "statistic_seq")
    @Column(name = "id", updatable = false, nullable = false)
    protected Integer id;

    @Column(name = "category", nullable = false, length = 30)
    protected String category;

    @Column(name = "name", nullable = false, length = 30)
    protected String name;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    private Office office;

    protected abstract void serializeValue(JsonGenerator generator)  throws IOException;

    public static class StatisticSerializer<T extends Statistic> extends StdSerializer<T> {

        public StatisticSerializer() {
            this(null);
        }

        public StatisticSerializer(Class<T> t) {
            super(t);
        }

        @Override
        public void serialize(T stat, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeStartObject();

            gen.writeNumberField("id", stat.id);
            gen.writeStringField("category", stat.category);
            gen.writeStringField(
                    "statisticType", stat.getClass().getSimpleName().toLowerCase().replace("statistic", "")
            );
            gen.writeStringField("name", stat.name);

            stat.serializeValue(gen);

            gen.writeEndObject();
        }

    }
}
