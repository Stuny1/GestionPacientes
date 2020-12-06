package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@Getter
@Setter
@Entity
@Table(name = "xy_plot_point")
public class XYPlotPoint extends PlotPoint {

    @Column(nullable = false)
    protected long xValue;
    @Column(nullable = false)
    protected long yValue;

    @Override
    void serializeToJson(JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        generator.writeObjectField("x", xValue);
        generator.writeNumberField("y", yValue);
        generator.writeEndObject();
    }

}
