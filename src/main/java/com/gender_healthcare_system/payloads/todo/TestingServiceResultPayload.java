package com.gender_healthcare_system.payloads.todo;

import com.gender_healthcare_system.entities.enu.GenderType;
import com.gender_healthcare_system.entities.enu.MeasureUnit;
import com.gender_healthcare_system.entities.enu.ResultType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceResultPayload implements Serializable {

    @Nationalized
    @NotBlank(message = "Title is required")
    @Length(min = 5, max = 50,
            message = "Test result title must be between 5 and 50 characters")
    private String title;

    @Nationalized
    @NotBlank(message = "Description is required")
    @Length(min = 5, max = 100,
            message = "Test result description must be between 5 and 100 characters")
    private String description;

    @NotNull(message = "Test result type is required")
    private ResultType type;

    @NotNull(message = "Test result gender type is required")
    private GenderType genderType;

    @Nationalized
    private MeasureUnit measureUnit;

    @DecimalMin(value = "0.0", message = "Min value must be equal to or greater than 0.0")
    @DecimalMax(value = "999.99", message = "Min value must be equal to or less than 999.99")
    @Digits(integer = 3, fraction = 2, message = "If provided, min test result value can " +
            "only have 1 to 3 integer digits and 4 decimal digits at most")
    private BigDecimal minValue;

    @DecimalMin(value = "0.0", message = "Min value must be equal to or greater than 0.0")
    @DecimalMax(value = "999.99", message = "Min value must be equal to or less than 999.99")
    @Digits(integer = 3, fraction = 3, message = "If provided, max test result value can " +
            "only have 1 to 3 integer digits and 4 decimal digits at most")
    private BigDecimal maxValue;
}
