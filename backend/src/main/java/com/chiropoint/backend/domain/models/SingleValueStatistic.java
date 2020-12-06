package com.chiropoint.backend.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.IOException;

@Getter
@Setter
@Entity
@Table(name = "single_value_statistic")
@JsonSerialize(using = Statistic.StatisticSerializer.class)
public class SingleValueStatistic extends Statistic {

    @Column(nullable = false)
    private String value;

    @Override
    protected void serializeValue(JsonGenerator generator) throws IOException {
        generator.writeStringField("value", value);
    }

}
