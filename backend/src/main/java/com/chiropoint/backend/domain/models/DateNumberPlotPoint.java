package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;

import javax.persistence.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Entity
@Table(name = "date_number_plot_point")
public class DateNumberPlotPoint extends XYPlotPoint {

    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date formattedXValue;

    public Date getFormattedXValue() {
        if (formattedXValue == null) {
            formattedXValue = new Date(xValue);
        }

        return formattedXValue;
    }

    @Override
    void serializeToJson(JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        generator.writeObjectField("x", getFormattedXValue());

        generator.writeNumberField("y", yValue);
        generator.writeEndObject();
    }
}
