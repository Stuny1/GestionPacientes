package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PlotPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plot_point_generator")
    @SequenceGenerator(name = "plot_point_generator", sequenceName = "plot_point_seq")
    @Column(name = "id", updatable = false, nullable = false)
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "statistic_id", referencedColumnName = "id")
    private PlottableStatistic statistic;

    abstract void serializeToJson(JsonGenerator generator) throws IOException;

}
