package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plottable_statistic")
@JsonSerialize(using = Statistic.StatisticSerializer.class)
public class PlottableStatistic extends Statistic {

    @Column(nullable = false)
    private int minimum;
    @Column(nullable = false)
    private int maximum;

    @OneToMany(mappedBy = "statistic")
    private List<PlotPoint> plotPoints = new ArrayList<>();

    public void addPlotPoint(PlotPoint point) {
        if (plotPoints.isEmpty()) {
            plotPoints.add(point);
        }

        if (!(point.getClass().equals(plotPoints.get(0).getClass()))) {
            return;
        }

        plotPoints.add(point);
        point.setStatistic(this);
    }

    @Override
    protected void serializeValue(JsonGenerator generator) throws IOException {
        generator.writeStringField("plotPointType",
                plotPoints.get(0).getClass().getSimpleName().toLowerCase().replace("plotpoint", ""));
        generator.writeArrayFieldStart("points");
        for (PlotPoint point: plotPoints) {
            point.serializeToJson(generator);
        }
        generator.writeEndArray();
    }

}
