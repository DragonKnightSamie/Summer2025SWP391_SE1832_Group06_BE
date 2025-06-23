package com.gender_healthcare_system.dtos.todo;

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
public class TestingServiceBookingResultDTO implements Serializable {

    private String title;

    private String description;

    private ResultType resultType;

    private GenderType genderType;

    private MeasureUnit measureUnit;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private String result;
}
