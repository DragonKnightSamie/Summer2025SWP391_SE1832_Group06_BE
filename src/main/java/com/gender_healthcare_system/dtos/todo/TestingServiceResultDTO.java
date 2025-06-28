package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gender_healthcare_system.entities.enu.GenderType;
import com.gender_healthcare_system.entities.enu.MeasureUnit;
import com.gender_healthcare_system.entities.enu.ResultType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceResultDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer serviceResultId;

    private String title;

    private String description;

    private ResultType resultType;

    private GenderType genderType;

    private MeasureUnit measureUnit;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private String result;

    public TestingServiceResultDTO(Integer serviceResultId, String title,
                                   String description, ResultType resultType,
                                   GenderType genderType, MeasureUnit measureUnit,
                                   BigDecimal minValue, BigDecimal maxValue) {
        this.serviceResultId = serviceResultId;
        this.title = title;
        this.description = description;
        this.resultType = resultType;
        this.genderType = genderType;
        this.measureUnit = measureUnit;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
